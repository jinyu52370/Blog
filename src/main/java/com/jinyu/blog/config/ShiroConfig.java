package com.jinyu.blog.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/17 20:11
 */
@Configuration
public class ShiroConfig {
    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        factoryBean.setSecurityManager(securityManager);
        //添加Shiro内置过滤器
        /*
         * Shiro内置过滤器，可以实现权限相关的拦截器
         *  常用的过滤器：
         *      anon：无需认证（登录）即可访问
         *      authc：必须认证才可以访问
         *      user：若使用了rememberMe，则可直接访问
         *      perms：该资源必须授予资源权限才可以访问
         *      role：该资源必须授予角色权限才可以访问
         */
        Map<String, String> filterMap = new LinkedHashMap<>();

        //授权过滤器，注意，授权过滤器需要在认证过滤器之前
        filterMap.put("/admin/blogs", "perms[admin]");
        filterMap.put("/admin/blogs/*", "perms[admin]");
        filterMap.put("/admin/setting", "perms[admin]");
        filterMap.put("/admin/setting/*", "perms[admin]");
        filterMap.put("/admin/tags", "perms[admin]");
        filterMap.put("/admin/tags/*", "perms[admin]");
        filterMap.put("/admin/types", "perms[admin]");
        filterMap.put("/admin/types/*", "perms[admin]");

        //认证过滤器
        filterMap.put("/admin/login", "anon");
        filterMap.put("/admin/logout", "anon");
        filterMap.put("/admin/*", "authc");


        //修改跳转的登录页面(默认 login.jsp)
        factoryBean.setLoginUrl("/admin");
        //修改跳转的授权页面
        factoryBean.setUnauthorizedUrl("/admin/unAuth");

        factoryBean.setFilterChainDefinitionMap(filterMap);
        return factoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("getUserRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    /**
     * 创建Realm
     */
    @Bean(name = "getUserRealm")
    public UserRealm getUserRealm(){
        return new UserRealm();
    }

    /**
     * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
     */
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
