package com.redis.example.locks;

import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

import java.time.Duration;

public interface DandelionCache extends Cache {

    void putLock(Object key, @Nullable Object value, @Nullable Duration ttl);

    void evictLock(Object key, @Nullable Object value);
}
