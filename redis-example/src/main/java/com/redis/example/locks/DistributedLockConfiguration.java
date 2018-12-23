package com.redis.example.locks;

import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class DistributedLockConfiguration {



    @Bean
    public DandelionCacheInterceptor distributedLockAdvice(
            StringRedisTemplate redisTemplate){
        DandelionCacheInterceptor interceptor = new DandelionCacheInterceptor();
        interceptor.setRedisTemplate(redisTemplate);
        //设置缓存操作源
        interceptor.setCacheOperationSource(cacheLockOperationSource());
        return interceptor;
    }

    @Bean
    public DistributedLockAdvisor distributedLockAdvisor(StringRedisTemplate redisTemplate){
        DistributedLockAdvisor distributedLockAdvisor = new DistributedLockAdvisor();
        distributedLockAdvisor.setAdvice(distributedLockAdvice(redisTemplate));
        return distributedLockAdvisor;
    }

    @Bean
    public AnnotationCacheOperationSource cacheLockOperationSource(){
        return new AnnotationCacheOperationSource(new DandelionCacheAnnotationParser());
    }

}
