package com.redis.example.locks;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.cache.Cache;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author 熊林鹏
 * @since 1.0.0
 */
public class DandelionRedisCache implements DandelionCache {

    /**
     * 缓存操作的名称。每一个缓存操作都有一个它自己的缓存{@link Cache},
     * 缓存操作的{@link Cache}存储在内存{@link java.util.concurrent.ConcurrentHashMap}中，
     * 以当前缓存操作名称作为键，每次进行缓存操作的时候，首先通过名称从系统缓存中加载。
     * @see org.springframework.cache.support.AbstractCacheManager#getCache(String)
     */
    private final String name;

    private final DandelionRedisCacheWriter cacheWriter;

    private final RedisCacheConfiguration cacheConfig;

    private final ConversionService conversionService;

    /**
     * Create new {@link DandelionRedisCache}.
     * @param name cache name.
     */
    public DandelionRedisCache(String name,DandelionRedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        this.name = name;
        this.cacheWriter = cacheWriter;
        this.cacheConfig = cacheConfig;
        this.conversionService = cacheConfig.getConversionService();
    }

    /**
     * 返回缓存的名称
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 返回底层本机缓存提供程序。
     */
    @Override
    public DandelionRedisCacheWriter getNativeCache() {
        return cacheWriter;
    }

    @Override
    public ValueWrapper get(Object key) {
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void putLock(Object key, Object value, @Nullable Duration ttl) {

        cacheWriter.putLock(name,createAndConvertCacheKey(key),serializeCacheValue(value),null);

    }

    @Override
    public void evictLock(Object key, Object value) {
        cacheWriter.removeLock(name,createAndConvertCacheKey(key),serializeCacheValue(value));
    }


    /**
     * Serialize the key.
     * @param cacheKey 永远不会是{@literal null}。
     * @return 从不是{@literal null}。
     */
    protected byte[] serializeCacheKey(String cacheKey){
        return ByteUtils.getBytes(cacheConfig.getKeySerializationPair().write(cacheKey));
    }

    /**
     * Serialize the value to cache.
     * @param value 永远不会是{@literal null}。
     * @return 从不是{@literal null}。
     */
    protected byte[] serializeCacheValue(Object value){

        return ByteUtils.getBytes(cacheConfig.getValueSerializationPair().write(value));
    }

    /**
     * 自定义钩子，用于在序列化缓存键之前创建缓存键。
     * @param key 永远不会是{@literal null}。
     * @return 从不是{@literal null}。
     */
    protected String createCacheKey(Object key) {
        //转换key为字符串
        String convertedKey = convertKey(key);
        //redis缓存配置是否配置使用key前缀
        if (!cacheConfig.usePrefix()) {
            return convertedKey;
        }
        //添加缓存key前缀
        return prefixCacheKey(convertedKey);
    }

    /**
     * 将{@code key}转换为用于创建缓存键的{@link String}表示形式。
     * @param key 永远不会是{@literal null}。
     * @return 从不是{@literal null}。
     */
    protected String convertKey(Object key){

        //创建key类型描述符
        TypeDescriptor source = TypeDescriptor.forObject(key);
        //判断key的类型是否能够转换为{@link String}类型
        if (conversionService.canConvert(source,TypeDescriptor.valueOf(String.class))){
            return conversionService.convert(key,String.class);
        }
        //获取key的toString方法
        Method toString = ReflectionUtils.findMethod(key.getClass(), "toString");
        //如果key的toString方法存在，并且key的父类不是Object，则调用其toString方法返回值作为key
        if (Objects.nonNull(toString) && !Object.class.equals(toString.getDeclaringClass())) {
            return key.toString();
        }
        throw new IllegalStateException(
                String.format("Cannot convert %s to String. Register a Converter or override toString().", source));
    }

    private byte[] createAndConvertCacheKey(Object key){
        return serializeCacheKey(createCacheKey(key));
    }

    private String prefixCacheKey(String key) {
        return cacheConfig.getKeyPrefixFor(name) + key;
    }
}
