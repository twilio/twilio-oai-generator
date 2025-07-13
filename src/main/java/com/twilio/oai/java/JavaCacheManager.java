package com.twilio.oai.java;


import java.util.EnumMap;
import java.util.Map;

/*
Usage:
CacheManager<String> cacheManager = CacheManager.getInstance();
cacheManager.put(CacheKey.ALL_MODELS, allModelsObject);
List<CodegenModel> allModels = cacheManager.get(CacheKey.ALL_MODELS);
boolean hasAllModels = cacheManager.containsKey(CacheKey.ALL_MODELS);
cacheManager.remove(CacheKey.ALL_MODELS);
int cacheSize = cacheManager.size();
cacheManager.clear();
 */
public class JavaCacheManager<V> {
    private final Map<CacheKey, V> cache;
    private static JavaCacheManager instance;

    private JavaCacheManager() {
        // EnumMap provides a good performance for enums
        cache = new EnumMap<>(CacheKey.class);
    }

    // Singleton access method
    public static synchronized JavaCacheManager getInstance() {
        if (instance == null) {
            instance = new JavaCacheManager();
        }
        return instance;
    }

    public void put(CacheKey key, V value) {
        cache.put(key, value);
    }
    public V get(CacheKey key) {
        return cache.get(key);
    }
    public V remove(CacheKey key) {
        return cache.remove(key);
    }
    public void clear() {
        cache.clear();
    }
    public boolean containsKey(CacheKey key) {
        return cache.containsKey(key);
    }
    public int size() {
        return cache.size();
    }
}