package com.wjm.bootbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author stephen wang
 */
@SpringBootApplication
@EnableScheduling
public class BootBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootBookApplication.class, args);
    }

}
