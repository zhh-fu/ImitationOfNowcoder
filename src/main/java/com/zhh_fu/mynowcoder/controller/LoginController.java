package com.zhh_fu.mynowcoder.controller;

import com.zhh_fu.mynowcoder.async.EventModel;
import com.zhh_fu.mynowcoder.async.EventProducer;
import com.zhh_fu.mynowcoder.async.EventType;
import com.zhh_fu.mynowcoder.dao.UserDAO;
import com.zhh_fu.mynowcoder.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
//负责登陆注册
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginController.class);


    //注册
    //向服务器端传递数据，使用POST方法
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletResponse httpServletResponse){
        try {
            Map<String, String> map = userService.register(username, password);
            //如果包含ticket证明注册成功，进行cookie的相应操作
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                httpServletResponse.addCookie(cookie);
                return "redirect:/index";
            }
            //否则重新注册
            else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }
        catch (Exception ex){
            logger.error("注册异常" + ex.getMessage());
            return "login";
        }
    }

    //登陆页面
    @RequestMapping(path = {"/userlogin/"}, method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse httpServletResponse){
        try {
            Map<String, Object> map = userService.login(username, password);
            //如果包含ticket证明用户名验证成功
            //同时将cookie上传给浏览器，跳转到首页
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                httpServletResponse.addCookie(cookie);

                //eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int) map.get("userId")).setExt("username",username).setExt("email","137588897@qq.com"));


                //如果next不为空，直接跳转到next对应页面
                if (!StringUtils.isBlank(next)){
                    return "redirect:" + next;
                }
                return "redirect:/index";
            }
            //否则重新登陆
            else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }
        catch (Exception ex){
            logger.error("登陆异常" + ex.getMessage());
            return "login";
        }
    }

    //注册登陆主页面，将数据传给reg页面
    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String regloginPage(Model model,
                               @RequestParam(value = "next",required = false) String next){
        model.addAttribute("next",next);
        return "login";
    }

    //退出功能
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/index";
    }
}
