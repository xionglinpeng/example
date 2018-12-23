package com.redis.example.locks;

import org.springframework.cache.annotation.CacheAnnotationParser;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * 缓存注解解析器
 * @see org.springframework.cache.annotation.SpringCacheAnnotationParser
 */
public class DandelionCacheAnnotationParser implements CacheAnnotationParser, Serializable {

    @Override
    public Collection<CacheOperation> parseCacheAnnotations(Class<?> type) {
        DefaultCacheConfig defaultConfig = new DefaultCacheConfig(type);
        return parseCacheAnnotations(defaultConfig,type);
    }

    @Override
    public Collection<CacheOperation> parseCacheAnnotations(Method method) {
        DefaultCacheConfig defaultConfig = new DefaultCacheConfig(method.getDeclaringClass());
        return parseCacheAnnotations(defaultConfig,method);
    }

    private Collection<CacheOperation> parseCacheAnnotations(DefaultCacheConfig defaultConfig, AnnotatedElement ae){
        Collection<CacheOperation> ops = parseCacheAnnotations(defaultConfig, ae, false);
        if (Objects.nonNull(ops) && ops.size() > 1) {
            Collection<CacheOperation> localOps = parseCacheAnnotations(defaultConfig, ae, true);
            if (Objects.nonNull(localOps))
                return localOps;
        }
        return ops;
    }

    private Collection<CacheOperation> parseCacheAnnotations(DefaultCacheConfig defaultConfig,AnnotatedElement ae,boolean localOnly){

        Collection<? extends Annotation> anns = localOnly ? AnnotatedElementUtils.getAllMergedAnnotations(ae, CacheLock.class) :
                AnnotatedElementUtils.findAllMergedAnnotations(ae, CacheLock.class);
        if (anns.isEmpty())
            return null;
        final Collection<CacheOperation> ops = new ArrayList<>(1);
        anns.stream().filter(ann->ann instanceof CacheLock).forEach(ann->{
            ops.add(parseLockAnnotation(ae,defaultConfig,(CacheLock) ann));
        });
        return ops;
    }

    private CacheLockOperation parseLockAnnotation(AnnotatedElement ae,DefaultCacheConfig defaultConfig,CacheLock cacheLock){


        CacheLockOperation.Builder builder = new CacheLockOperation.Builder();
        builder.setName(ae.toString());
        builder.setCacheNames(cacheLock.cacheNames());
        builder.setCondition(cacheLock.condition());
////        builder.setUnless(cacheLock.unless());
        builder.setKey(cacheLock.key());
        builder.setKeyGenerator(cacheLock.keyGenerator());
        builder.setCacheManager(cacheLock.cacheManager());
        builder.setCacheResolver(cacheLock.cacheResolver());
// //       builder.setSync(cacheLock.sync());

        builder.setExpire(cacheLock.expire());
        builder.setTimeUnit(cacheLock.timeUnit());


        defaultConfig.applyDefault(builder);
        CacheLockOperation op = builder.build();

        return op;
    }

    /**
     * 为给定的缓存操作集提供默认设置。
     */
    private static class DefaultCacheConfig {

        private final Class<?> target;

        private String[] cacheNames;

        private String keyGenerator;

        private String cacheManager;

        private String cacheResolver;

        private boolean initialized = false;

        public DefaultCacheConfig(Class<?> target) {
            this.target = target;
        }

        /**
         * 对指定的{@link CacheOperation.Builder}应用默认值。
         * @param builder 要更新的操作构建器
         */
        public void applyDefault(CacheOperation.Builder builder){
            if (!initialized) {
                CacheConfig annotation = AnnotatedElementUtils.findMergedAnnotation(this.target,CacheConfig.class);
                if (Objects.nonNull(annotation)) {
                    this.cacheManager = annotation.cacheManager();
                    this.cacheNames = annotation.cacheNames();
                    this.cacheResolver = annotation.cacheResolver();
                    this.keyGenerator = annotation.keyGenerator();
                }
                this.initialized = true;
            }
            //设置cacheNames
            if (builder.getCacheNames().isEmpty() && Objects.nonNull(this.cacheNames)) {
                builder.setCacheNames(this.cacheNames);
            }
            //设置keyGenerator
            if (!StringUtils.hasText(builder.getKey()) && !StringUtils.hasText(builder.getKeyGenerator())
                    && StringUtils.hasText(this.keyGenerator)) {
                builder.setKeyGenerator(keyGenerator);
            }

            if (StringUtils.hasText(builder.getCacheManager()) || StringUtils.hasText(builder.getCacheResolver())) {
                //其中一个设置好了，所以我们不应该设置任何东西
            }
            else if (StringUtils.hasText(this.cacheResolver)) {
                builder.setCacheResolver(this.cacheResolver);
            }
            else if (StringUtils.hasText(this.cacheManager)) {
                builder.setCacheManager(this.cacheManager);
            }
        }
    }
}
