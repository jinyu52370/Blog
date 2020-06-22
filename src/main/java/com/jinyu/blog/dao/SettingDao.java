package com.jinyu.blog.dao;

import com.jinyu.blog.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SettingDao extends JpaRepository<Setting, Long> {
    @Query("SELECT s.aboutMeImg FROM Setting s WHERE s.id = ?1")
    String getAboutMeImgById(Long id);
}
