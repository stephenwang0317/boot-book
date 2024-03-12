package com.wjm.bootbook.config.resolver;

import com.wjm.bootbook.entity.dto.JwtDTO;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author stephen wang
 */
@Component
public class JwtArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String HEADER_KEY = "user-info";

    @Override
    public boolean supportsParameter(
            @Nonnull MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return parameterType == JwtDTO.class;
    }

    @Override
    public Object resolveArgument(
            @Nonnull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @Nonnull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        return request.getAttribute(HEADER_KEY);
    }
}
