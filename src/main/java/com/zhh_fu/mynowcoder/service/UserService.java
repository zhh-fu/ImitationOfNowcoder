package com.zhh_fu.mynowcoder.service;

import com.zhh_fu.mynowcoder.dao.UserDAO;
import com.zhh_fu.mynowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User getUserById(int id){
        return userDAO.selectById(id);
    }
}
