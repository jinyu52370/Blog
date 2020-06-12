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
    protected static final String BLOG_STATIC_IMAGES_PATH = "WEB-INF/classes/static/images/";

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

    @GetMapping("/setting/{userId}")
    public String setting(@PathVariable Long userId, Model model, HttpSession session) {
        User user = userService.getUser(userId);
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        return "admin/setting";
    }

    @PostMapping("/setting")
    public String setting(User user, HttpServletRequest request){
        String base64Avatar = user.getAvatar();
        //base64头像非空时，存储头像并将路径赋值给user.avatar
        if (base64Avatar != null && !"".equals(base64Avatar)) {
            //剪切后的头像的文件名
            String avatarOriginalFilename = "头像" + user.getId() + ".jpg";
            //剪切后头像的绝对路径
            String avatarAbsolutePath = request.getServletContext().getRealPath("/") + BLOG_STATIC_IMAGES_PATH  + avatarOriginalFilename;
            try {
                // Base64解码
                if (base64Avatar.contains(",")) {
                    String encodedImg = base64Avatar.split(",")[1];
                    byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
                    //将base64编码转换为的字节数组decodedImg，再转换为MultipartFile对象
                    MultipartFile avatarFile = new MockMultipartFile(MediaType.APPLICATION_OCTET_STREAM_VALUE, new ByteArrayInputStream(decodedImg));
                    //写文件
                    avatarFile.transferTo(new File(avatarAbsolutePath));
                    //给当前用户设置剪切后的头像
                    user.setAvatar("/images/" + avatarOriginalFilename);
                }
            } catch (Exception e) {
                throw new RuntimeException("文件上传失败");
            }
        }
        return "redirect:/admin/setting/" + userService.saveUser(user).getId();
    }

    //获得密码的Md5值
    public static void main(String[] args) {
        System.out.println(DigestUtils.md5DigestAsHex("root".getBytes()));
    }
}
