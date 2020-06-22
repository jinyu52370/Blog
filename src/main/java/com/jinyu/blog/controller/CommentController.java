package com.jinyu.blog.controller;

import com.jinyu.blog.entity.Comment;
import com.jinyu.blog.entity.User;
import com.jinyu.blog.service.BlogService;
import com.jinyu.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/6 17:44
 */
@Controller
public class CommentController {
    @Resource
    private CommentService commentService;
    @Resource
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String publish(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));

        comment.setAvatar(avatar);

        User user = (User) session.getAttribute("user");
        if (user != null){
            comment.setAvatar(user.getSetting().getAvatar());
            comment.setAdminComment(true);
        }

        commentService.save(comment);
        return "redirect:/comments/" + blogId;
    }
}
