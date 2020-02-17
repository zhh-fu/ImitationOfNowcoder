package com.zhh_fu.mynowcoder.service;

import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.dao.LoginTicketDAO;
import com.zhh_fu.mynowcoder.dao.UserDAO;
import com.zhh_fu.mynowcoder.model.LoginTicket;
import com.zhh_fu.mynowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    //用户注册
    //使用map是因为返回的内容有很多种
    public Map<String,String> register(String name, String passowrd){
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(name)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(passowrd)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(name);
        if (user != null) {
            map.put("msg","用户名已被注册");
            return map;
        }

        user = new User();
        user.setName(name);
        //随机salt
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));

        //密码使用md5加密，将password和salt结合在一起
        user.setPassword(MynowcoderUtil.MD5(passowrd + user.getSalt()));
        userDAO.addUser(user);

        //将登陆用户和ticket结合，并将ticket返回给浏览器
        //通过HttpResponse返回给cookie
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    //用户登陆
    public Map<String,Object> login(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("msg","用户名不存在");
            return map;
        }

        //此处进行密码匹配
        //如果不匹配则报用户名或密码错误
        if(!MynowcoderUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg","用户名或密码错误");
            return map;
        }

        //将登陆用户和ticket结合，并将ticket返回给浏览器
        //通过HttpResponse返回给cookie
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        map.put("userId", user.getId());
        return map;
    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        //当前时间偏移一百天为ticket过期时间
        Date ticketTime = new Date();
        //此处直接使用3600*24*1000*10会被认为是int类型而截断
        long addTime = 3600*24*1000*10L;
        ticketTime.setTime(addTime + ticketTime.getTime());
        loginTicket.setExpired(ticketTime);
        //状态0为正常，状态1过期
        loginTicket.setStatus(0);
        //使用UUID生成，并去掉所有的-
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    //用户登出
    public void logout(String ticket){
        loginTicketDAO.updateTicket(ticket,1);
    }
    //通过id获取用户信息
    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public User getUserByName(String name){
        return userDAO.selectByName(name);
    }
}
