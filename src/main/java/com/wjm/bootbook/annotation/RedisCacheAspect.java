package com.wjm.bootbook.annotation;

import com.wjm.bootbook.entity.common.ResponseResult;
import com.wjm.bootbook.entity.dto.RedisToMqDTO;
import com.wjm.bootbook.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Random;

import static com.wjm.bootbook.entity.common.RabbitMqConstant.*;

/**
 * @author stephen wang
 */
@Component
@Aspect
@Slf4j
public class RedisCacheAspect {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    private static final Integer EXPIRE_MINUTES = 60;
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDisc = new DefaultParameterNameDiscoverer();
    private final Random random = new Random();

    private Method getTargetMethod(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return signature.getMethod();
    }

    @Around(value = "@annotation(cacheInfo)",
            argNames = "pjp,cacheInfo")
    public ResponseResult<?> around(ProceedingJoinPoint pjp, RedisCache cacheInfo) {
        String key = cacheInfo.key();
        Expression expression = parser.parseExpression(key);
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = pjp.getArgs();
        String[] names = nameDisc.getParameterNames(getTargetMethod(pjp));
        for (int i = 0; i < names.length; i++) {
            context.setVariable(names[i], args[i]);
        }
        String string;
        try {
            string = expression.getValue(context).toString();
        } catch (NullPointerException e) {
            throw new RuntimeException("SpEL 解析失败");
        }

        //log.info("around: " + key);
        if (cacheInfo.read() == 1) {
            return readDatabase(pjp, string);
        } else {
            return writeDatabase(pjp, string);
        }
    }

    private ResponseResult<?> readDatabase(ProceedingJoinPoint pjp, String key) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o != null) {
            RedisToMqDTO dto = new RedisToMqDTO(
                    key,
                    null,
                    Duration.ofMinutes(EXPIRE_MINUTES + random.nextInt(10)));
            rabbitTemplate.convertAndSend(REDIS_EXCHANGE_NAME, KEYWORD_EXPIRE, dto);
            return ResponseResult.success(o);
        }
        try {
            ResponseResult<?> result = (ResponseResult<?>) pjp.proceed();
            RedisToMqDTO dto = new RedisToMqDTO(
                    key,
                    result.getData(),
                    Duration.ofMinutes(EXPIRE_MINUTES + random.nextInt(10)));
            rabbitTemplate.convertAndSend(REDIS_EXCHANGE_NAME, KEYWORD_ADD, dto);
            return result;
        } catch (Throwable throwable) {
            throw new CustomException(throwable.getMessage());
        }
    }

    private ResponseResult<?> writeDatabase(ProceedingJoinPoint pjp, String key) {
        try {
            ResponseResult<?> result = (ResponseResult<?>) pjp.proceed();
            rabbitTemplate.convertAndSend(REDIS_EXCHANGE_NAME, KEYWORD_DELETE, key);
            return result;
        } catch (Throwable e) {
            throw new CustomException(e.getMessage());
        }
    }
}
