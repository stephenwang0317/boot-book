package com.wjm.bootbook.bean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

@SpringBootTest
@EnableScheduling
public class TokenBucketTest {

    @Autowired
    RedisTokenBucket bucket;

    @Test
    void test() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            Thread.sleep(100);
            System.out.println(i + " " + bucket.getToken() + " ");
        }
    }


    @Test
    void use() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            boolean token = bucket.getToken();
            System.out.println("token " + token + " " + i);
            Thread.sleep(Duration.ofMillis(500));
        }
    }

}
