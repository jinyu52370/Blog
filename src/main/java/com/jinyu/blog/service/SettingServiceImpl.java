package com.jinyu.blog.service;

import com.jinyu.blog.dao.SettingDao;
import com.jinyu.blog.entity.Setting;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SettingServiceImpl implements SettingService{
    @Resource
    private SettingDao settingDao;

    @Override
    public Setting getSetting(Long id) {
        return settingDao.getOne(id);
    }

    @Override
    public Setting saveSetting(Setting setting) {
        return settingDao.save(setting);
    }

    @Override
    public String getAboutMeImgById(Long id) {
        return settingDao.getAboutMeImgById(id);
    }
}
