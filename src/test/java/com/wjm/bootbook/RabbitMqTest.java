package com.wjm.bootbook;

import com.wjm.bootbook.entity.pojo.Article;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.wjm.bootbook.entity.common.RabbitMqConstant.*;

@SpringBootTest
public class RabbitMqTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void test01() {
        Article article = new Article();
        article.setArtAuthor(100000L);
        article.setArtTitle("人工智能技术助力医疗影像诊断，提高医疗精准度");
        article.setArtContent("最新研究显示，人工智能技术在医疗影像诊断领域发挥着越来越重要的作用，可以帮助医生提高诊断准确性和精准度。通过深度学习和机器学习算法，人工智能系统能够快速准确地识别医学影像中的异常情况，并提供可靠的诊断建议。这一技术不仅可以加速医疗诊断过程，还可以降低误诊率，提升医疗治疗效果。医学界普遍认为，人工智能技术将成为未来医疗影像诊断的重要发展方向。");
        rabbitTemplate.convertAndSend(ES_EXCHANGE_NAME, KEYWORD_ADD, article);
    }
}
