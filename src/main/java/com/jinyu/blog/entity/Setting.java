package com.jinyu.blog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/21 22:17
 */
@Entity
@Table(name = "setting")
public class Setting {
    @Id
    @GeneratedValue
    private Long id;
    private String avatar;
    private String welcomeImg;
    private String aboutMeImg;
    private String backgroundImg;
    private String backgroundVideo;

    public Setting() {
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", welcomeImg='" + welcomeImg + '\'' +
                ", aboutMeImg='" + aboutMeImg + '\'' +
                ", backgroundImg='" + backgroundImg + '\'' +
                ", backgroundVideo='" + backgroundVideo + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWelcomeImg() {
        return welcomeImg;
    }

    public void setWelcomeImg(String welcomeImg) {
        this.welcomeImg = welcomeImg;
    }

    public String getAboutMeImg() {
        return aboutMeImg;
    }

    public void setAboutMeImg(String aboutMeImg) {
        this.aboutMeImg = aboutMeImg;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getBackgroundVideo() {
        return backgroundVideo;
    }

    public void setBackgroundVideo(String backgroundVideo) {
        this.backgroundVideo = backgroundVideo;
    }
}
