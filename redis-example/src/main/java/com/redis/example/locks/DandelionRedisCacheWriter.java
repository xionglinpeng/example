package com.redis.example.locks;

import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.time.Duration;

public interface DandelionRedisCacheWriter extends RedisCacheWriter {

    void putLock(String name, byte[] key, byte[] value, @Nullable Duration ttl);

    void removeLock(String name, byte[] key, byte[] value);

}
