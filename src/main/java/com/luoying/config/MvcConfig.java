package com.luoying.config;

import com.luoying.utils.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对swagger的请求不进行拦截
        String[] excludePatterns = new String[]{"/swagger-resources/**", "/swagger-resources","/webjars/**", "/v2/**", "/swagger-ui.html/**",
                "/api", "/api-docs", "/api-docs/**", "/doc.html","/doc.html#/**"};
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns(
                "/user/login",
                "/user/register",
                "/user/searchByTags",
                "/user/recommend"
        ).excludePathPatterns(excludePatterns).order(0);
    }
}
