package com.jinyu.blog.service;

import com.jinyu.blog.dao.UserDao;
import com.jinyu.blog.entity.Blog;
import com.jinyu.blog.entity.User;
import com.jinyu.blog.exception.NotFoundException;
import com.jinyu.blog.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/5/31 22:26
 */
@Service
public class UserServiceImpl implements UserService{
    @Resource
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        return userDao.findByUsernameAndPassword(username, password);
    }

    @Override
    public User saveUser(User user) {
        User u = userDao.getOne(user.getId());
        if (u == null){
            throw new NotFoundException("不存在该用户");
        }
        BeanUtils.copyProperties(user, u, MyBeanUtils.getNullPropertyNames(user));
        u.setUpdateTime(new Date());
        return userDao.save(u);
    }

    @Override
    public User getUser(Long id) {
        return userDao.getOne(id);
    }

    @Override
    public User getByUsername(String username) {
        return userDao.getByUsername(username);
    }

    @Override
    public String getTypeById(Long id) {
        return userDao.getTypeById(id);
    }

    @Override
    public Long getSettingIdById(Long id) {
        return userDao.getSettingIdById(id);
    }

    @Override
    public Long getSettingIdByType(String type) {
        return userDao.getSettingIdByType(type);
    }
}
