package com.wjm.bootbook.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author stephen wang
 */
@Configuration
@Slf4j
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.uris}")
    private String esUrl;

    private RestHighLevelClient restHighLevelClient;

    @Bean
    RestHighLevelClient restHighLevelClient() {
        restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(HttpHost.create(esUrl)));
        return restHighLevelClient;
    }

    @PreDestroy
    private void doDestroy() {
        log.info("RestHighLevelClient 准备销毁");
        if (restHighLevelClient != null) {
            try {
                restHighLevelClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}