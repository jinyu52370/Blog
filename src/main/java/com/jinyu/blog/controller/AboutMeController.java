package com.jinyu.blog.controller;

import com.jinyu.blog.service.SettingService;
import com.jinyu.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/7 18:12
 */
@Controller
@RequestMapping("/aboutMe")
public class AboutMeController {
    @Resource
    private SettingService settingService;
    @Resource
    private UserService userService;

    @GetMapping
    public String aboutMe(){
        return "aboutMe";
    }

    @GetMapping("/img")
    @ResponseBody
    public String aboutMeImg(){
        return settingService.getAboutMeImgById(userService.getSettingIdByType("admin"));
    }
}
