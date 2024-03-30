package com.wjm.bootbook.bean.listener;

import com.wjm.bootbook.bean.BaiduCheckService;
import com.wjm.bootbook.config.MinioConfig;
import com.wjm.bootbook.entity.dto.review.BaiduReviewDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wjm.bootbook.entity.common.RabbitMqConstant.*;

/**
 * @author stephen wang
 */
@Component
@Slf4j
public class MinioListener {

    @Autowired
    BaiduCheckService checkService;

    @Autowired
    MinioConfig minioConfig;

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = MINIO_EXCHANGE_NAME),
            value = @Queue(name = MINIO_UPLOAD_QUEUE_NAME),
            key = {KEYWORD_UPLOAD}
    ))
    public void listenUploadFile(String url) throws Exception {
        String[] split = url.split("/");
        String objName = split[split.length - 1];
        BaiduReviewDTO dto = checkService.imageReview(url);
        if (!dto.getConclusion().equals(BaiduCheckService.REVIEW_OK)) {
            minioConfig.removeObject(objName);
            log.info(objName + " has been deleted, because: "
                    + dto.getReason());
        }
    }
}
