package com.wjm.bootbook.bean.listener;

import com.wjm.bootbook.entity.dto.RedisToMqDTO;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.wjm.bootbook.entity.common.RabbitMqConstant.*;

/**
 * @author stephen wang
 */
@Component
public class RedisListener {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = REDIS_ADD_QUEUE_NAME),
            exchange = @Exchange(name = REDIS_EXCHANGE_NAME),
            key = {KEYWORD_ADD}
    ))
    public void listenAddOptions(RedisToMqDTO dto) {
        String key = dto.getKey();
        Object value = dto.getValue();
        Duration duration = dto.getDuration();
        redisTemplate
                .opsForValue()
                .set(key, value, duration);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = REDIS_DELETE_QUEUE_NAME),
            exchange = @Exchange(name = REDIS_EXCHANGE_NAME),
            key = {KEYWORD_DELETE}
    ))
    public void listenDeleteOptions(String key) {
        redisTemplate.delete(key);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = REDIS_EXPIRE_QUEUE_NAME),
            exchange = @Exchange(name = REDIS_EXCHANGE_NAME),
            key = {KEYWORD_EXPIRE}
    ))
    public void listenExpireOptions(RedisToMqDTO dto) {
        String key = dto.getKey();
        Duration duration = dto.getDuration();
        redisTemplate
                .expire(key, duration);
    }
}
