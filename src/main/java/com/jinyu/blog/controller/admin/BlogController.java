package com.jinyu.blog.controller.admin;

import com.jinyu.blog.entity.Blog;
import com.jinyu.blog.entity.Tag;
import com.jinyu.blog.entity.User;
import com.jinyu.blog.service.BlogService;
import com.jinyu.blog.service.TagService;
import com.jinyu.blog.service.TypeService;
import com.jinyu.blog.util.UploadFileUtils;
import com.jinyu.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Value;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/1 14:53
 */
@Controller
@RequestMapping("/admin")
public class BlogController {
    public static final String INPUT = "admin/blogs-input";
    public static final String LIST = "admin/blogs";
    public static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Resource
    private BlogService blogService;
    @Resource
    private TypeService typeService;
    @Resource
    private TagService tagsService;
    @Value("${serverIp}")
    private String serverIp;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(
            size = 8,
            sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(
            size = 8,
            sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagsService.listTag());
    }

    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private String saveOrEdit(Blog blog,
                              HttpSession session,
                              RedirectAttributes attributes,
                              BindingResult result,
                              HttpServletRequest request) {
        if (typeService.findByName(blog.getTitle()) != null) {
            result.rejectValue("name", "nameError", "已存在该标题");
        }
        if (blog.getTitle() == null || "".equals(blog.getTitle())) {
            result.rejectValue("name", "nameError", "博客标题不能为空");
        }
        if (blog.getContent() == null || "".equals(blog.getContent())) {
            result.rejectValue("name", "nameError", "博客内容不能为空");
        }
        if (blog.getType() == null) {
            result.rejectValue("name", "nameError", "博客分类不能为空");
        }
        if (blog.getPicture() == null || "".equals(blog.getPicture())) {
            result.rejectValue("name", "nameError", "博客首图不能为空");
        }
        if (blog.getDescription() == null || "".equals(blog.getDescription())) {
            result.rejectValue("name", "nameError", "博客描述不能为空");
        }
        if (result.hasErrors()) {
            return INPUT;
        }
        //添加用户
        blog.setUser((User) session.getAttribute("user"));
        //添加分类
        blog.setType(typeService.getType(blog.getType().getId()));
        //处理新加的标签
        List<Long> tagIdListFromDataBase = tagsService.listTag().stream().map(Tag::getId).collect(Collectors.toList());
        List<Long> tagIdListToSave = new ArrayList<>();
        Long newTagId = null;
        String tagIdsStr = blog.getTagIds();
        if (tagIdsStr != null && !"".equals(tagIdsStr)) {
            for (String split : tagIdsStr.split(",")) {
                //如果标签首字符不是数字 或 标签在数据库中不存在，则存入数据库
                if (!Character.isDigit(split.charAt(0)) || !tagIdListFromDataBase.contains(Long.valueOf(split))) {
                    //若是新添加的标签，则其id在该标签存入数据库tag表中后，加入需要保存的tagIdListToSave
                    newTagId = tagsService.saveTag(new Tag(split)).getId();
                    tagIdListToSave.add(newTagId);
                    continue;
                }
                //若不是新添加的标签，则其id通过 "," 分割后直接加入需要保存的tagIdListToSave
                tagIdListToSave.add(Long.valueOf(split));
            }
        }
        blog.setTags(tagsService.listTag(tagIdListToSave));
        //添加首图
        String base64Picture = blog.getPicture();
        try {
            if (base64Picture != null && !"".equals(base64Picture) && base64Picture.contains(",")) {
                String fastDfsFileId = UploadFileUtils.getStorageClient1().upload_file1(UploadFileUtils.base64Decode(base64Picture), "jpg", null);
                blog.setPicture(serverIp + fastDfsFileId);
            }
        } catch (Exception e) {
            throw new RuntimeException("首图上传失败");
        }

        //更新
        if (blog.getId() != null) {
            if (blogService.updateBlog(blog) != null) {
                attributes.addFlashAttribute("message", "更新成功");
            } else {
                attributes.addFlashAttribute("message", "更新失败");
            }
        }
        //添加
        if (blog.getId() == null) {
            if (blogService.saveBlog(blog) != null) {
                attributes.addFlashAttribute("message", "添加成功");
            } else {
                attributes.addFlashAttribute("message", "添加失败");
            }
        }
        return REDIRECT_LIST;
    }

    @PostMapping("/blogs")
    public String save(Blog blog,
                       HttpSession session,
                       RedirectAttributes attributes,
                       BindingResult result,
                       HttpServletRequest request){
        return saveOrEdit(blog, session, attributes, result, request);
    }

    @PostMapping("/blogs/{id}")
    public String edit(@PathVariable Long id,
                       Blog blog,
                       HttpSession session,
                       RedirectAttributes attributes,
                       BindingResult result,
                       HttpServletRequest request){
        return saveOrEdit(blog, session, attributes, result, request);
    }

    @GetMapping("/blogs/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }
    
    @GetMapping("/blogs/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }
}
