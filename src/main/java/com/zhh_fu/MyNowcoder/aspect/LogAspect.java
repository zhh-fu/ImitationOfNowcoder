package com.zhh_fu.MyNowcoder.aspect;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component //组件的方式插入
public class LogAspect {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //括号中为类的全限定名，意思是访问IndexController中所有的方法时都会调用下面的方法
    @Before("execution(* com.zhh_fu.MyNowcoder.controller.IndexController.*(..))")
    public void beforeMethod(){
        logger.info("before method:");
    }

    @After("execution(* com.zhh_fu.MyNowcoder.controller.IndexController.*(..))")
    public void afterMethodd(){
        logger.info("after method:");
    }
}
