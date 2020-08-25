package com.jinyu.blog.controller.admin;

import com.jinyu.blog.config.UploadFileUtils;
import com.jinyu.blog.entity.Setting;
import com.jinyu.blog.entity.User;
import com.jinyu.blog.service.SettingService;
import com.jinyu.blog.service.UserService;
import org.csource.fastdfs.StorageClient1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
    @Resource
    private Jedis jedis;
    @Value("${fdfsPre}")
    private String fdfsPre;

    @GetMapping("/setting/{userId}")
    public String setting(@PathVariable Long userId, Model model, HttpSession session) {
        User user = userService.getUser(userId);
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        return "admin/setting";
    }

    @PostMapping("/setting")
    public String setting(User user, Setting setting) {
        String base64Avatar = setting.getAvatar();
        String base64WelcomeImg = setting.getWelcomeImg();
        String base64AboutMeImg = setting.getAboutMeImg();
        StorageClient1 storageClient1;
        //头像
        try {
            if (base64Avatar != null && !"".equals(base64Avatar) && base64Avatar.contains(",")) {
                storageClient1 = UploadFileUtils.getStorageClient1();
                //上传到fastDFS服务器
                String fastDfsFileId = storageClient1.upload_file1(UploadFileUtils.base64Decode(base64Avatar), "jpg", null);
                //删除历史头像
                storageClient1.delete_file1(jedis.get("avatar"));
                //将新id保存在redis中
                jedis.set("avatar", fastDfsFileId);
                setting.setAvatar(fdfsPre + fastDfsFileId);
            }
        } catch (Exception e) {
            throw new RuntimeException("头像上传失败");
        }
        //欢迎页图片
        try {
            if (base64WelcomeImg != null && !"".equals(base64WelcomeImg) && base64WelcomeImg.contains(",")) {
                storageClient1 = UploadFileUtils.getStorageClient1();
                String fastDfsFileId = storageClient1.upload_file1(UploadFileUtils.base64Decode(base64WelcomeImg), "jpg", null);
                //删除历史欢迎页图片
                storageClient1.delete_file1(jedis.get("welcomeImg"));
                //将新id保存在redis中
                jedis.set("welcomeImg", fastDfsFileId);
                setting.setWelcomeImg(fdfsPre + fastDfsFileId);
            }
        } catch (Exception e) {
            throw new RuntimeException("欢迎页图片上传失败");
        }
        //关于我图片
        try {
            if (base64AboutMeImg != null && !"".equals(base64AboutMeImg) && base64AboutMeImg.contains(",")) {
                storageClient1 = UploadFileUtils.getStorageClient1();
                String fastDfsFileId = storageClient1.upload_file1(UploadFileUtils.base64Decode(base64AboutMeImg), "jpg", null);
                //删除历史关于我图片
                storageClient1.delete_file1(jedis.get("aboutMeImg"));
                //将新id保存在redis中
                jedis.set("aboutMeImg", fastDfsFileId);
                setting.setAboutMeImg(fdfsPre + fastDfsFileId);
            }
        } catch (Exception e) {
            throw new RuntimeException("关于我图片上传失败");
        }

        setting.setId(userService.getSettingIdById(user.getId()));
        settingService.saveSetting(setting);
        return "redirect:/admin/setting/" + userService.saveUser(user).getId();
    }
}
