package com.jinyu.blog.controller.admin;

import com.jinyu.blog.entity.User;
import com.jinyu.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
//        User user = userService.checkUser(username, DigestUtils.md5DigestAsHex(password.getBytes()));
//        if (user == null){
//            attributes.addFlashAttribute("message", "用户名或密码错误");
//            return "redirect:/admin";
//        }
//        user.setPassword(null);
//        session.setAttribute("user", user);
//        return "admin/index";

        //使用shiro编写认证操作
        //1.获取subject
        Subject subject = SecurityUtils.getSubject();
        //2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //3.执行登录方法
        try {
            subject.login(token);
            //无异常，登录成功
            User user = userService.checkUser(username, DigestUtils.md5DigestAsHex(password.getBytes()));
            user.setPassword(null);
            session.setAttribute("user", user);
            return "admin/index";
        } catch (UnknownAccountException e) {
            //有异常，登录失败
            //1.UnknownAccountException 用户名不存在
            attributes.addFlashAttribute("message", "用户名不存在");
            return "redirect:/admin";
        } catch (IncorrectCredentialsException e){
            //2.IncorrectCredentialsException 密码错误
            attributes.addFlashAttribute("message", "密码错误");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        SecurityUtils.getSubject().logout();
        return "redirect:/admin";
    }

    @GetMapping("/unAuth")
    public String unAuth(){
        return "error/unAuth";
    }
}
