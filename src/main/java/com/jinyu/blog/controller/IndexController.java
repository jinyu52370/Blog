package com.jinyu.blog.controller;

import com.jinyu.blog.service.BlogService;
import com.jinyu.blog.service.TagService;
import com.jinyu.blog.service.TypeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/5/30 13:05
 */
@Controller
public class IndexController {
    @Resource
    private BlogService blogService;
    @Resource
    private TypeService typeService;
    @Resource
    private TagService tagService;


    @GetMapping("/")
    public String index(@PageableDefault(
            size = 8,
            sort = {"createTime"},
            direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listBlogTop(8));
        return "index";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(
            size = 8,
            sort = {"createTime"},
            direction = Sort.Direction.DESC) Pageable pageable, Model model, @RequestParam String search) {

        model.addAttribute("page", blogService.listBlog(search, pageable));
        model.addAttribute("search", search);
        return "search";
    }

    @GetMapping("/footer/newestBlogs")
    public String newestBlogs(Model model){
        model.addAttribute("newestBlogs", blogService.listBlogTop(3));
        return "_fragments :: newestBlogList";
    }
}
