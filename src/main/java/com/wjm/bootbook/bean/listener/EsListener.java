package com.wjm.bootbook.bean.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjm.bootbook.entity.pojo.Article;
import com.wjm.bootbook.exception.CustomException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.wjm.bootbook.entity.common.RabbitMqConstant.*;
import static com.wjm.bootbook.entity.common.ElasticSearchConstant.*;

/**
 * @author stephen wang
 */
@Component
public class EsListener {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    private final ObjectMapper om = new ObjectMapper();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ES_ADD_QUEUE_NAME),
            exchange = @Exchange(name = ES_EXCHANGE_NAME),
            key = {KEYWORD_ADD}
    ))
    public void listenAddOptions(Article article) {
        IndexRequest request = new IndexRequest(ES_ARTICLE_INDEX_NAME).id(article.getArtId().toString());
        try {
            request.source(om.writeValueAsString(article), XContentType.JSON);
        } catch (JsonProcessingException e) {
            throw new CustomException(e.getMessage());
        }

        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new CustomException("Sync ElasticSearch Error: ADD");
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ES_DELETE_QUEUE_NAME),
            exchange = @Exchange(name = ES_EXCHANGE_NAME),
            key = {KEYWORD_DELETE}
    ))
    public void listenDeleteOptions(Long id) {
        DeleteRequest request = new DeleteRequest(ES_ARTICLE_INDEX_NAME, id.toString());
        try {
            restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new CustomException("Sync ElasticSearch Error: DELETE");
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ES_UPDATE_QUEUE_NAME),
            exchange = @Exchange(name = ES_EXCHANGE_NAME),
            key = {KEYWORD_UPDATE}
    ))
    public void listenUpdateOptions(Article article) {
        listenAddOptions(article);
    }
}
