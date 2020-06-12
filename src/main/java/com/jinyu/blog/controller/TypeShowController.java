package com.jinyu.blog.controller;

import com.jinyu.blog.entity.Type;
import com.jinyu.blog.service.BlogService;
import com.jinyu.blog.service.TypeService;
import com.jinyu.blog.vo.BlogQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/7 15:29
 */
@Controller
public class TypeShowController {
    @Resource
    private TypeService typeService;
    @Resource
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(
            size = 8,
            sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                        Model model,
                        @PathVariable Long id) {

        List<Type> types = typeService.listTypeTop(Integer.MAX_VALUE);
        if (id == -1) {
            id = types.get(0).getId();
        }
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.listBlog(pageable, new BlogQuery(id)));
        model.addAttribute("activeTypeId", id);
        return "types";
    }
}
