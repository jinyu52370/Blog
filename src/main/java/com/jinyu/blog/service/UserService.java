package com.jinyu.blog.service;

import com.jinyu.blog.entity.User;

public interface UserService {
    User checkUser(String username, String password);

    User saveUser(User user);

    User getUser(Long id);

    User getByUsername(String username);

    String getTypeById(Long id);

    Long getSettingIdById(Long id);

    Long getSettingIdByType(String type);
}
