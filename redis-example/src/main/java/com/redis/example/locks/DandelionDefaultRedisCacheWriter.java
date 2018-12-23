package com.redis.example.locks;

import java.time.Duration;

public class DandelionDefaultRedisCacheWriter implements DandelionRedisCacheWriter {

    @Override
    public void putLock(String name, byte[] key, byte[] value, Duration ttl) {

    }

    @Override
    public void removeLock(String name, byte[] key, byte[] value) {

    }

    @Override
    public void put(String name, byte[] key, byte[] value, Duration ttl) {

    }

    @Override
    public byte[] get(String name, byte[] key) {
        return new byte[0];
    }

    @Override
    public byte[] putIfAbsent(String name, byte[] key, byte[] value, Duration ttl) {
        return new byte[0];
    }

    @Override
    public void remove(String name, byte[] key) {

    }

    @Override
    public void clean(String name, byte[] pattern) {

    }
}
