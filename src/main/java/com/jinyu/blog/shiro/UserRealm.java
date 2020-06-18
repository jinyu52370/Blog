package com.jinyu.blog.shiro;

import com.jinyu.blog.entity.User;
import com.jinyu.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/17 9:38
 */
@Component
public class UserRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 执行授权逻辑
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("执行授权逻辑");
        //给资源授权
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //添加资源的授权字符串
        Subject subject = SecurityUtils.getSubject();
        Long id = (Long) subject.getPrincipal();

        authorizationInfo.addStringPermission(userService.getTypeById(id));
        return authorizationInfo;
    }

    /**
     * 执行认证逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("执行认证逻辑");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        String inputUsername = token.getUsername();
        User user = userService.getByUsername(inputUsername);
        if (user == null){
            return null;
        }
        token.setPassword(DigestUtils.md5DigestAsHex(new String(token.getPassword()).getBytes()).toCharArray());
        return new SimpleAuthenticationInfo(user.getId(), user.getPassword(), "");
    }
}
