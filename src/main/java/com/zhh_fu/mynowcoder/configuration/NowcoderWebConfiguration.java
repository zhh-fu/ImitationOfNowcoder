package com.zhh_fu.mynowcoder.configuration;

import com.zhh_fu.mynowcoder.interceptor.LoginRequiredInterceptor;
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

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //上面为正常登陆用户的拦截器
        //下面为未登录用户拦截器
        //注意两者的前后关系
        //去除掉静态页面
        registry.addInterceptor(passportInterceptor).excludePathPatterns(EXCLUDE_PATH);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*").excludePathPatterns(EXCLUDE_PATH);

        //super.addInterceptors(registry);
    }
}
