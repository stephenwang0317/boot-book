package com.wjm.bootbook.config;

import com.wjm.bootbook.entity.dto.JwtDTO;
import com.wjm.bootbook.utils.JwtUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author stephen wang
 */
@Configuration
public class AuthenticInterceptor implements WebMvcConfigurer {
    public static final String HEADER_KEY = "user-info";

    private static final String[] EXCLUDE_PATH = new String[]{"/user/login", "/user/register"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(
                    @Nonnull HttpServletRequest request,
                    @Nonnull HttpServletResponse response,
                    @Nonnull Object handler) throws Exception {
                if (!(handler instanceof HandlerMethod)) {
                    return true;
                }

                String token = request.getHeader("token");
                if (!StringUtils.hasText(token)) {
                    // TODO 全局异常处理
                }

                JwtDTO jwtUser = JwtUtils.getUserFromJwt(token);
                request.setAttribute(HEADER_KEY, jwtUser);
                return true;
            }
        }).excludePathPatterns(EXCLUDE_PATH);
    }

    @Override
    public void addArgumentResolvers(@Nonnull List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new HandlerMethodArgumentResolver() {
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
                    WebDataBinderFactory binderFactory) throws Exception {
                HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
                return (JwtDTO) request.getAttribute(HEADER_KEY);
            }
        });
    }
}
