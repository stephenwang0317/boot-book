package com.wjm.bootbook.config.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.wjm.bootbook.entity.common.ExceptionMessage;
import com.wjm.bootbook.entity.dto.JwtDTO;
import com.wjm.bootbook.exception.CustomException;
import com.wjm.bootbook.exception.JwtException;
import com.wjm.bootbook.utils.JwtUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author stephen wang
 */
@Component
public class AuthenticInterceptor implements HandlerInterceptor {
    public static final String HEADER_KEY = "user-info";

    @Override
    public boolean preHandle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            throw new CustomException(ExceptionMessage.NOT_LOGIN.getMessage());
        }

        try {
            JwtDTO jwtUser = JwtUtils.getUserFromJwt(token);
            // 可以使用ThreadLocal
            request.setAttribute(HEADER_KEY, jwtUser);
        } catch (JWTVerificationException e) {
            if (e instanceof TokenExpiredException) {
                throw new JwtException(e.getMessage());
            } else {
                throw new CustomException(ExceptionMessage.JWT_VERIFICATION_ERROR.getMessage());
            }
        }
        return true;
    }
}
