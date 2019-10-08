package com.lizhy.util;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流工具类
 * Created by lizhiyang on 2017-01-03 17:16.
 */
public class RateLimitUtils {
    private static Logger logger = LoggerFactory.getLogger(RateLimitUtils.class);
    private static Map<String, RateLimiter> limitCache = new ConcurrentHashMap<>();

    public static RateLimiter getResource(String resource, double qps) {
        RateLimiter limit = limitCache.get(resource);
        if(limit == null) {
            synchronized(RateLimitUtils.class) {
                limit = limitCache.get(resource);
                if(limit == null) {
                    limit = RateLimiter.create(qps);
                    limitCache.put(resource, limit);
                    logger.info("create rate limiter for key:"+resource);
                }
            }
        }
        return limitCache.get(resource);
    }

    public static void clear() {
        limitCache.clear();
    }
}
