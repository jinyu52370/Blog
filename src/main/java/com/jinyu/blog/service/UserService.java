package com.jinyu.blog.service;

import com.jinyu.blog.entity.User;

public interface UserService {
    User checkUser(String username, String password);

    User saveUser(User user);

    User getUser(Long id);
}
