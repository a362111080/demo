package com.zero.egg.config;

import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MybatisPlusConfig
 * @Description MybatisPlus配置文件
 * @Author lyming
 * @Date 2018/11/1 17:10
 **/
@Configuration
public class MybatisPlusConfig {

    /***
     * plus 的性能优化
     * @return
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        /** 打印出SQL是否格式化 */
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }
}