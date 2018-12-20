package com.redis.example.locks;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    /**
     * Alias for {@link #cacheNames()}.
     */
    @AliasFor("cacheNames")
    String[] value() default {};

    /**
     * 存储方法调用结果的缓存的名称。
     * 名称可用于确定目标缓存(或多个缓存)，匹配特定bean定义的限定符值或bean名称。
     * @see #value()
     * @see CacheConfig#cacheNames()
     */
    @AliasFor("value")
    String[] cacheNames() default {};

    /**
     * 注意，因为是自己扩展封装的，所以IDE不会有相应的提示
     * @return
     */
    String key() default "";

    /**
     *
     * @see CacheConfig#keyGenerator()
     */
    String keyGenerator() default "";

    /**
     *
     * @see CacheConfig#cacheManager()
     */
    String cacheManager() default "";

    /**
     * 要使用的自定义{@link org.springframework.cache.interceptor.CacheResolver}
     * 的bean名。
     * @see CacheConfig#cacheResolver()
     */
    String cacheResolver() default "";

    String condition() default "";

    /**
     * 锁有效期，默认5秒。
     */
    int expire() default 5;

    /**
     * 锁有效期的时间单位，默认为秒。
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
