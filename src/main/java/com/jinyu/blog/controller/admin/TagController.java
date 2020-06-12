package com.jinyu.blog.controller.admin;

import com.jinyu.blog.entity.Tag;
import com.jinyu.blog.service.TagService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/2 15:45
 */
@Controller
@RequestMapping("/admin")
public class TagController {
    @Resource
    private TagService tagService;

    /**
     * 标签列表
     * @return 标签列表页面
     */
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC)
                                Pageable pageable, Model model){
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }

    /**
     * 添加按钮
     * @return 添加标签页面
     */
    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    /**
     * 编辑标签
     * @return 添加标签页面
     */
    @GetMapping("/tags/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    /**
     * 插入或更新标签
     * @return 标签列表页面
     */
    private String saveOrEdit(Tag tag, BindingResult result, RedirectAttributes attributes){
        if (tagService.findByName(tag.getName()) != null){
            result.rejectValue("name", "nameError", "已存在该标签");
        }
        if (tag.getName() == null || "".equals(tag.getName())){
            result.rejectValue("name", "nameError", "标签名不能为空");
        }
        if (result.hasErrors()){
            return "admin/tags-input";
        }

        //更新
        if (tag.getId() != null) {
            if (tagService.saveTag(tag) != null) {
                attributes.addFlashAttribute("message", "更新成功");
            } else {
                attributes.addFlashAttribute("message", "更新失败");
            }
        }
        //添加
        if (tag.getId() == null){
            if (tagService.saveTag(tag) != null) {
                attributes.addFlashAttribute("message", "添加成功");
            } else {
                attributes.addFlashAttribute("message", "添加失败");
            }
        }

        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/save")
    public String save(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        return saveOrEdit(tag, result, attributes);
    }

    @PostMapping("/tags/edit/{id}")
    public String edit(@PathVariable Long id, @Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        return saveOrEdit(tag, result, attributes);
    }

    @GetMapping("/tags/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }
}
