package com.jinyu.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/7 18:12
 */
@Controller
public class AboutMeController {
    @GetMapping("/aboutMe")
    public String aboutMe(){
        return "aboutMe";
    }
}
