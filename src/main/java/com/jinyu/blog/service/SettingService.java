package com.jinyu.blog.service;

import com.jinyu.blog.entity.Setting;
import com.jinyu.blog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SettingService {
    Setting getSetting(Long id);

    Setting saveSetting(Setting setting);

    String getAboutMeImgById(Long id);
}
