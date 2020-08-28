package com.jinyu.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/8/22 16:40
 */
@Configuration
public class JedisConfig extends CachingConfigurerSupport {
    //    private Logger logger = LoggerFactory.getLogger(JedisConfig.class);
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.database}")
    private String database;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public JedisPool redisPoolFactory() {
//        logger.info("JedisPool注入成功！");
//        logger.info("redis地址：" + host + ":" + port);
        return new JedisPool(new JedisPoolConfig(), host, port, 300, password, Integer.parseInt(database));
    }

    @Bean
    public Jedis jedis() {
        return redisPoolFactory().getResource();
    }
}
