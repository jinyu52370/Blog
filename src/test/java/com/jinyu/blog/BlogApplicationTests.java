package com.jinyu.blog;

import com.jinyu.blog.dao.BlogDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
class BlogApplicationTests {
    @Resource
    BlogDao blogDao;

    @Test
    public void replaceFastDfs() {
        String aliyun = "http://182.92.228.183:8888/";
        HashMap<Long, String> map = new HashMap<>();

        blogDao.findAll().forEach(blog -> {
            String[] split = blog.getPicture().split("http://jinyu.host:8888/");
            map.put(blog.getId(), aliyun + split[1]);
        });
        for (Long id : map.keySet()) {
            System.out.println(blogDao.replaceFastDfs(id, map.get(id)));
        }
    }


    @Resource
    Jedis jedis;

    @Test
    public void redisTest() {

        String avatar = jedis.get("avatar");
        String aboutMeImg = jedis.get("aboutMeImg");
        String welcomeImg = jedis.get("welcomeImg");

        System.out.println(avatar);
        System.out.println(aboutMeImg);
        System.out.println(welcomeImg);
    }
}
