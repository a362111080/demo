package com.zero.egg.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 强指定redis的JedisPool接口构造函数，这样才能在centos成功创建jedispool
 *
 * @ClassName JedisPoolWriper
 * @Author lyming
 * @Date 2019/4/3 18:47
 **/
public class JedisPoolWriper {
    /**
     * Redis连接池对象
     */
    private JedisPool jedisPool;

    public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host, final int port, final int timeout, final String password) {
        try {
            jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
