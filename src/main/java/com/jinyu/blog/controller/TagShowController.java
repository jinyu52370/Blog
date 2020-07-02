package com.jinyu.blog.controller;

import com.jinyu.blog.entity.Tag;
import com.jinyu.blog.service.BlogService;
import com.jinyu.blog.service.TagService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/7 15:29
 */
@Controller
public class TagShowController {
    @Resource
    private TagService tagService;
    @Resource
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(
            size = 8,
            sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                        Model model,
                        @PathVariable Long id) {

        List<Tag> tags = tagService.listTagTop(Integer.MAX_VALUE);
        if (id == -1) {
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(id, pageable));
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
