package com.zero.egg.config;

import com.zero.egg.cache.JedisPoolWriper;
import com.zero.egg.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 对标redis.properties
 *
 * @ClassName RedisConfiguration
 * @Author lyming
 * @Date 2019/4/9 23:15
 **/
@Configuration
public class RedisConfiguration {
    @Value("${redis.hostname}")
    private String hostname;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.pool.maxActive}")
    private int maxTotal;
    @Value("${redis.pool.maxIdle}")
    private int maxIdle;
    @Value("${redis.pool.maxWait}")
    private long maxWaitMillis;
    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.timeout}")
    private int timeout;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Autowired
    private JedisPoolWriper jedisPoolWriper;
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 创建redis连接池的设置
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis对象实例
        jedisPoolConfig.setMaxTotal(maxTotal);
        //连接池中最多可空闲maxIdle个连接
        //表示即便没有数据库连接时依旧可以保持maxIdle个连接,而不被清除,处于待命状态
        //最大等待时间:当没有可用连接时
        jedisPoolConfig.setMaxIdle(maxIdle);
        //最大等待时间,当没有可用连接时,连接池等待连接被归还的最大时间(单位毫秒),超过时间抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        //在获取连接的时候检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    /**
     * 创建Redis连接池,并做相关配置
     *
     * @return
     */
    @Bean(name = "jedisPoolWriper")
    public JedisPoolWriper createJedisPoolWriper() {
        JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostname, port, timeout, password);
        return jedisPoolWriper;
    }

    /**
     * 创建Redis工具类,封装好Rdis的连接以进行相关的操作
     *
     * @return
     */
    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil() {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWriper);
        return jedisUtil;
    }

    /**
     * redis的key操作
     */
    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKeys() {
        JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
        return jedisKeys;
    }

    /**
     * redis的String操作
     */
    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createJedisStrings() {
        JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
        return jedisStrings;
    }

    /**
     * redis的List操作
     */
    @Bean(name = "jedisLists")
    public JedisUtil.Lists createJedisLists() {
        JedisUtil.Lists jedisLists = jedisUtil.new Lists();
        return jedisLists;
    }

    /**
     * redis的Sets操作
     */
    @Bean(name = "jedisSets")
    public JedisUtil.Sets createJedisSets() {
        JedisUtil.Sets jedisSets = jedisUtil.new Sets();
        return jedisSets;
    }

    /**
     * redis的Sets操作
     */
    @Bean(name = "jedisSortSets")
    public JedisUtil.SortSets createJedisSortSets() {
        JedisUtil.SortSets jedisSortSets = jedisUtil.new SortSets();
        return jedisSortSets;
    }

    /**
     * redis的Hash操作
     */
    @Bean(name = "jedisHash")
    public JedisUtil.Hash createJedisHash() {
        JedisUtil.Hash jedisHash = jedisUtil.new Hash();
        return jedisHash;
    }
}
