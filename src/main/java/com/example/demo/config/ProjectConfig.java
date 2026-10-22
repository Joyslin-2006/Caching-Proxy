package com.example.demo.config;

import com.example.demo.Proxy.DirectHttpProxy;
import com.example.demo.Proxy.HttpProxy;
import com.example.demo.cache.InMemoryHttpProxy;
import com.example.demo.cache.RedisCacheHttpProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class ProjectConfig {
    @Bean
    HttpProxy httpProxy(){
        return new DirectHttpProxy();
    }
//
//    @Bean
//    @Primary
//    HttpProxy chacheHttpProxy(HttpProxy proxy){
//        return new InMemoryHttpProxy(proxy);
//    }

    @Bean
    @Primary
    HttpProxy chacheHttpProxy(HttpProxy delegate, RedisConnectionFactory factory){
        return new RedisCacheHttpProxy(delegate, factory);
    }
}
