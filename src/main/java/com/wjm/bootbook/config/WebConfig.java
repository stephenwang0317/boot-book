package com.wjm.bootbook.config;

import com.wjm.bootbook.config.interceptor.AuthenticInterceptor;
import com.wjm.bootbook.config.interceptor.TrafficControlInterceptor;
import com.wjm.bootbook.config.resolver.JwtArgumentResolver;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @author stephen wang
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] EXCLUDE_PATH = {"/user/login", "/user/register"};

    @Autowired
    AuthenticInterceptor authenticInterceptor;
    @Autowired
    TrafficControlInterceptor trafficControlInterceptor;
    @Autowired
    JwtArgumentResolver jwtArgumentResolver;


    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        //System.out.println(Arrays.deepToString(EXCLUDE_PATH));
        registry.addInterceptor(authenticInterceptor).excludePathPatterns(EXCLUDE_PATH);
        registry.addInterceptor(trafficControlInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtArgumentResolver);
    }
}
