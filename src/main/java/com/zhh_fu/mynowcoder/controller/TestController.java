package com.zhh_fu.mynowcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Controller
public class TestController {

    @RequestMapping(path = {"/i"},method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession){
        return "hello fuzhihang!" + httpSession.getAttribute("msg");
    }

    //此处的method方法很重要，区分get和post
    @RequestMapping(path = {"/profile/{group}/{userId}"},method = {RequestMethod.GET})
    @ResponseBody
    public String profile(//路径参数，需要与路径形参一致
                          @PathVariable("userId") int userId,
                          @PathVariable("group") int groupId,
                          //请求参数，也就是通过？和&传递的参数
                          @RequestParam(value = "key",required = false) String key,
                          @RequestParam(value = "type",defaultValue = "1") int type){
        return String.format("Profile Page of %d / %d %s %d", userId, groupId, key, type);
    }

    //路径不会直接渲染而是通过模板去渲染
    //没有ResponseBody，它会去templates中找相应的模板
    //2.2.4boot对应的freemarker默认的后缀名是.ftlh
    //可在配置文件中更改默认后缀
    @RequestMapping(path = {"/vm"},method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("key","value");
//        List<String> list = new ArrayList<>();
//        list.add("2343e");
//        list.add("213dssr");
//        list.add("23ucd");
        model.addAttribute("zxc","list");
        return "home";
    }

    @RequestMapping(path = {"/request"},method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession httpSession){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> head = request.getHeaderNames();
        while (head.hasMoreElements()){
            String name = head.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }

        sb.append(request.getMethod() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURL() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        response.addHeader("myId","fuzhihang");
        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"},method = {RequestMethod.GET})
    public String redirect(@PathVariable("code") int code,
                          HttpSession httpSession){
        httpSession.setAttribute("msg"," jump from redirect!");
        return "redirect:/index";
    }
}
