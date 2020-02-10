package com.zhh_fu.MyNowcoder.controller;

import com.zhh_fu.MyNowcoder.service.Test1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {
    @Autowired
    Test1Service test1Service;
}
