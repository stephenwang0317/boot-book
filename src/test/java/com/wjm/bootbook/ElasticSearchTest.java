package com.wjm.bootbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjm.bootbook.entity.pojo.Article;
import com.wjm.bootbook.service.ArticleService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class ElasticSearchTest {
    @Autowired
    ArticleService articleService;

    @Value("${spring.elasticsearch.uris}")
    private String es_url;

    @Test
    public void test01() {
        try (RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(HttpHost.create(es_url)))) {
            List<Article> list = articleService.list();
            BulkRequest bulkRequest = new BulkRequest();
            for (Article article : list) {
                IndexRequest request =
                        new IndexRequest("article").id(String.valueOf(article.getArtId()));
                ObjectMapper om = new ObjectMapper();
                request.source(om.writeValueAsString(article), XContentType.JSON);
                bulkRequest.add(request);
            }
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(bulk);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
