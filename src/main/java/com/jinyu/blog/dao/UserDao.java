package com.jinyu.blog.dao;

import com.jinyu.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);

    User getByUsername(String username);

    @Query("SELECT u.type FROM User u WHERE u.id = ?1")
    String getTypeById(Long id);

    @Query("SELECT u.setting.id FROM User u WHERE u.id = ?1")
    Long getSettingIdById(Long id);

    @Query("SELECT u.setting.id FROM User u WHERE u.type = ?1")
    Long getSettingIdByType(String type);
}
