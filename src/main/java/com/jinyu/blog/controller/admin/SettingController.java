package com.jinyu.blog.controller.admin;

import com.jinyu.blog.entity.User;
import com.jinyu.blog.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/13 9:26
 */
@Controller
@RequestMapping("/admin")
public class SettingController {
    protected static final String BLOG_STATIC_IMAGES_PATH = "WEB-INF/classes/static/images/";

    @Resource
    private UserService userService;

    @GetMapping("/setting/{userId}")
    public String setting(@PathVariable Long userId, Model model, HttpSession session) {
        User user = userService.getUser(userId);
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        return "admin/setting";
    }

    @PostMapping("/setting")
    public String setting(User user, String welcome, String aboutMe, HttpServletRequest request){
        //头像
        String base64Avatar = user.getAvatar();
        //base64头像非空时，存储头像并将路径赋值给user.avatar
        if (base64Avatar != null && !base64Avatar.contains("images")&& !"".equals(base64Avatar)) {
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
        //欢迎页图片
        if (welcome != null && !welcome.contains("images")&& !"".equals(welcome)) {
            String welcomeOriginalFilename = "welcome.jpg";
            String welcomeAbsolutePath = request.getServletContext().getRealPath("/") + BLOG_STATIC_IMAGES_PATH  + welcomeOriginalFilename;
            try {
                if (welcome.contains(",")) {
                    String encodedImg = welcome.split(",")[1];
                    byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
                    MultipartFile welcomeFile = new MockMultipartFile(MediaType.APPLICATION_OCTET_STREAM_VALUE, new ByteArrayInputStream(decodedImg));
                    welcomeFile.transferTo(new File(welcomeAbsolutePath));
                }
            } catch (Exception e) {
                throw new RuntimeException("欢迎页图片上传失败");
            }
        }
        //关于我页面
        if (aboutMe != null && !aboutMe.contains("images")&& !"".equals(aboutMe)) {
            String aboutMeOriginalFilename = "aboutMe.jpg";
            String aboutMeAbsolutePath = request.getServletContext().getRealPath("/") + BLOG_STATIC_IMAGES_PATH  + aboutMeOriginalFilename;
            try {
                if (aboutMe.contains(",")) {
                    String encodedImg = aboutMe.split(",")[1];
                    byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
                    MultipartFile aboutMeFile = new MockMultipartFile(MediaType.APPLICATION_OCTET_STREAM_VALUE, new ByteArrayInputStream(decodedImg));
                    aboutMeFile.transferTo(new File(aboutMeAbsolutePath));
                }
            } catch (Exception e) {
                throw new RuntimeException("关于我图片上传失败");
            }
        }
        return "redirect:/admin/setting/" + userService.saveUser(user).getId();
    }
}
