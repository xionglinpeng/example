package com.redis.example.locks;

import org.springframework.cache.interceptor.CacheOperation;

public class CacheLockOperation extends CacheOperation {

    /**
     * Create a new {@link CacheLockOperation} instance from the given builder.
     */
    public CacheLockOperation(CacheLockOperation.Builder builder){
        super(builder);
    }

    /**
     * A builder that can be used to create a {@link CacheLockOperation}.
     */
    public static class Builder extends CacheOperation.Builder {

        @Override
        public CacheLockOperation build() {
            return new CacheLockOperation(this);
        }
    }
}
