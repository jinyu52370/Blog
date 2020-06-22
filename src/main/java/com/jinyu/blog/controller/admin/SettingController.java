package com.jinyu.blog.controller.admin;

import com.jinyu.blog.entity.Setting;
import com.jinyu.blog.entity.User;
import com.jinyu.blog.service.SettingService;
import com.jinyu.blog.service.UserService;
import com.jinyu.blog.util.UploadFileUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/13 9:26
 */
@Controller
@RequestMapping("/admin")
public class SettingController {
    @Resource
    private UserService userService;
    @Resource
    private SettingService settingService;
    @Value("${serverIp}")
    private String serverIp;

    @GetMapping("/setting/{userId}")
    public String setting(@PathVariable Long userId, Model model, HttpSession session) {
        User user = userService.getUser(userId);
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        return "admin/setting";
    }

    @PostMapping("/setting")
    public String setting(User user, Setting setting){
        String base64Avatar = setting.getAvatar();
        String base64WelcomeImg = setting.getWelcomeImg();
        String base64AboutMeImg = setting.getAboutMeImg();
        //头像
        try {
            if (base64Avatar != null && !"".equals(base64Avatar) && base64Avatar.contains(",")) {
                //上传到fastDFS服务器
                String fastDfsFileId = UploadFileUtils.getStorageClient1().upload_file1(UploadFileUtils.base64Decode(base64Avatar), "jpg", null);
                setting.setAvatar(serverIp + fastDfsFileId);
            }
        } catch (Exception e) {
            throw new RuntimeException("头像上传失败");
        }
        //欢迎页图片
        try {
            if (base64WelcomeImg != null && !"".equals(base64WelcomeImg) && base64WelcomeImg.contains(",")) {
                String fastDfsFileId = UploadFileUtils.getStorageClient1().upload_file1(UploadFileUtils.base64Decode(base64WelcomeImg), "jpg", null);
                setting.setWelcomeImg(serverIp + fastDfsFileId);
            }
        } catch (Exception e) {
            throw new RuntimeException("欢迎页图片上传失败");
        }
        //关于我图片
        try {
            if (base64AboutMeImg != null && !"".equals(base64AboutMeImg) && base64AboutMeImg.contains(",")) {
                String fastDfsFileId = UploadFileUtils.getStorageClient1().upload_file1(UploadFileUtils.base64Decode(base64AboutMeImg), "jpg", null);
                setting.setAboutMeImg(serverIp + fastDfsFileId);
            }
        } catch (Exception e) {
            throw new RuntimeException("关于我图片上传失败");
        }

        setting.setId(userService.getSettingIdById(user.getId()));
        settingService.saveSetting(setting);
        return "redirect:/admin/setting/" + userService.saveUser(user).getId();
    }
}
