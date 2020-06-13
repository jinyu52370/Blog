package com.jinyu.blog.controller.admin;

import com.jinyu.blog.entity.User;
import com.jinyu.blog.exception.NotFoundException;
import com.jinyu.blog.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/5/31 22:40
 */
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Resource
    private UserService userService;

    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user = userService.checkUser(username, DigestUtils.md5DigestAsHex(password.getBytes()));
        if (user == null){
            attributes.addFlashAttribute("message", "用户名或密码错误");
            return "redirect:/admin";
        }
        user.setPassword(null);
        session.setAttribute("user", user);
        return "admin/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    //获得密码的Md5值
    public static void main(String[] args) {
        System.out.println(DigestUtils.md5DigestAsHex("root".getBytes()));
    }
}
