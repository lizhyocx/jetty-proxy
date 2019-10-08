package com.lizhy.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class CacheUtils {
	final static LoadingCache<String,String> cache = CacheBuilder.newBuilder()
			.maximumSize(1001)						 // 缓存队列长度固定为1000
			.expireAfterWrite(10, TimeUnit.MINUTES)  // 缓存失效时间固定为10分钟
			.build(new CacheLoader<String, String>() {
				@Override
				public String load(String key) throws Exception {
					return "NoN";
				}
			});
	public static void put(String key,String value) throws Exception{
		cache.put(key,value);
	}
	public static String get(String key) throws Exception{
		return cache.get(key);
	}
	public static String getIfPresent(String key) throws Exception{
		return cache.getIfPresent(key);
	}
	public static ConcurrentMap asMap(){
		return cache.asMap();
	}
}
