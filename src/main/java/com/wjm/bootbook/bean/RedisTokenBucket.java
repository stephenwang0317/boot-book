package com.wjm.bootbook.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.List;

/**
 * @author stephen wang
 */
@Component
@Slf4j
public class RedisTokenBucket {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_BUCKET_PREFIX = "token:bucket";
    private static final Integer TOKEN_BUCKET_MAX_SIZE = 10;
    private static final Integer TOKEN_GENERATE_RATE = 1;
    private static final Long INIT_TIME = Instant.parse("2024-01-01T00:00:00Z").toEpochMilli();
    private static final String REDIS_SCRIPT = """
            local key = KEYS[1]
            local rate = ARGV[1]
            local bucket_size = tonumber(ARGV[2])
            local now = ARGV[3]
            
            local current = tonumber(redis.call('get', key .. ':remain') or '0')
            local last_refreshed = tonumber(redis.call('get', key .. ':last_refreshed') or '0')
            local time_passed = math.max(now - last_refreshed, 0)
            local new_tokens = math.floor(time_passed * rate)
            local tokens = math.min(current + new_tokens, bucket_size)
            if new_tokens > 0 then
            \tredis.call('set', key .. ':remain', tokens)
            \tredis.call('set', key .. ':last_refreshed', now)
            end

            if tokens > 0 then
            \tredis.call('decr', key .. ':remain')
            \treturn 1
            else
            \treturn 0
            end
            """;

    public boolean getToken() {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(REDIS_SCRIPT, Long.class);
        List<String> keys = List.of(TOKEN_BUCKET_PREFIX);
        Double curTime = (Instant.now().toEpochMilli() - INIT_TIME) / 1000.0;
        // rate, bucket size, now
        Long execute = redisTemplate.execute(redisScript,
                keys,
                TOKEN_GENERATE_RATE,
                TOKEN_BUCKET_MAX_SIZE,
                curTime
        );
        //System.out.println(execute);
        return !ObjectUtils.isEmpty(execute) && execute == 1;
    }

}
