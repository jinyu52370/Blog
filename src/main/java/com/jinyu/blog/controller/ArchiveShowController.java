package com.jinyu.blog.controller;

import com.jinyu.blog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/7 17:19
 */
@Controller
public class ArchiveShowController {
    @Resource
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model){
        model.addAttribute("archiveMap", blogService.archiveBlog());
        model.addAttribute("blogCount", blogService.countArchiveBlog());
        return "archives";
    }
}
