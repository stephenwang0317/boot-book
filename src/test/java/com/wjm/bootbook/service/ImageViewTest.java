package com.wjm.bootbook.service;

import com.wjm.bootbook.bean.BaiduCheckService;
import com.wjm.bootbook.entity.dto.review.BaiduReviewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageViewTest {
    @Autowired
    BaiduCheckService checkService;

    @Test
    void test01() {
        BaiduReviewDTO baiduReviewDTO = checkService.imageReview("http://123.56.143.129:9002/boot-book-images/avatar-100000.png");
        System.out.println(baiduReviewDTO);
    }
}
