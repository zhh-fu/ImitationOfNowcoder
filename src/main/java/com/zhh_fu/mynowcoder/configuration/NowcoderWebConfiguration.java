package com.zhh_fu.mynowcoder.configuration;

import com.zhh_fu.mynowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Component
public class NowcoderWebConfiguration implements WebMvcConfigurer {
    private static final List<String> EXCLUDE_PATH= Arrays.asList("/css/**","/js/**","/images/**","/scripts/**","/styles/**");


    @Autowired
    PassportInterceptor passportInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor).excludePathPatterns(EXCLUDE_PATH);

        //super.addInterceptors(registry);
    }
}
