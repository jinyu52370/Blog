package com.jinyu.blog.controller.admin;

import com.jinyu.blog.entity.Type;
import com.jinyu.blog.service.TypeService;
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
 * @date 2020/6/1 16:39
 */
@Controller
@RequestMapping("/admin")
public class TypeController {
    @Resource
    private TypeService typeService;

    /**
     * 分类列表
     * @return 分类列表页面
     */
    @GetMapping("/types")
    public String types(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model){
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    /**
     * 添加按钮
     * @return 添加分类页面
     */
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    /**
     * 编辑分类
     * @return 添加分类页面
     */
    @GetMapping("/types/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }

    /**
     * 插入或更新分类
     * @return 分类列表页面
     */
    private String saveOrEdit(Type type, BindingResult result, RedirectAttributes attributes){
        if (typeService.findByName(type.getName()) != null){
            result.rejectValue("name", "nameError", "已存在该分类");
        }
        if (type.getName() == null || "".equals(type.getName())){
            result.rejectValue("name", "nameError", "分类名不能为空");
        }
        if (result.hasErrors()){
            return "admin/types-input";
        }

        //更新
        if (type.getId() != null) {
            if (typeService.saveType(type) != null) {
                attributes.addFlashAttribute("message", "更新成功");
            } else {
                attributes.addFlashAttribute("message", "更新失败");
            }
        }
        //添加
        if (type.getId() == null){
            if (typeService.saveType(type) != null) {
                attributes.addFlashAttribute("message", "添加成功");
            } else {
                attributes.addFlashAttribute("message", "添加失败");
            }
        }

        return "redirect:/admin/types";
    }

    @PostMapping("/types/save")
    public String save(@Valid Type type, BindingResult result, RedirectAttributes attributes){
        return saveOrEdit(type, result, attributes);
    }

    @PostMapping("/types/edit/{id}")
    public String edit(@PathVariable Long id, @Valid Type type, BindingResult result, RedirectAttributes attributes){
        return saveOrEdit(type, result, attributes);
    }

    @GetMapping("/types/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }
}
