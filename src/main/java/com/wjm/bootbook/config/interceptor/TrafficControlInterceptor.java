package com.wjm.bootbook.config.interceptor;

import com.wjm.bootbook.component.RedisTokenBucket;
import com.wjm.bootbook.entity.common.ExceptionMessage;
import com.wjm.bootbook.entity.dto.JwtDTO;
import com.wjm.bootbook.exception.CustomException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author stephen wang
 */
@Component
public class TrafficControlInterceptor implements HandlerInterceptor {
    @Resource
    RedisTokenBucket redisTokenBucket;

    @Override
    public boolean preHandle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (redisTokenBucket.getToken()) {
            return true;
        } else {
            throw new CustomException(ExceptionMessage.SERVER_TOO_BUSY.getMessage());
        }
    }
}
