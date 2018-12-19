package com.redis.example.locks;

import org.springframework.cache.interceptor.CacheOperation;

import java.util.concurrent.TimeUnit;

public class CacheLockOperation extends CacheOperation {

    private Integer expire;

    private TimeUnit timeUnit;

    public Integer getExpire() {
        return expire;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * Create a new {@link CacheLockOperation} instance from the given builder.
     */
    public CacheLockOperation(CacheLockOperation.Builder builder){
        super(builder);
        this.expire = builder.expire;
        this.timeUnit = builder.timeUnit;
    }

    /**
     * A builder that can be used to create a {@link CacheLockOperation}.
     */
    public static class Builder extends CacheOperation.Builder {

        private Integer expire;

        private TimeUnit timeUnit;

        public void setExpire(Integer expire) {
            this.expire = expire;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }

        @Override
        public CacheLockOperation build() {
            return new CacheLockOperation(this);
        }

    }
}
