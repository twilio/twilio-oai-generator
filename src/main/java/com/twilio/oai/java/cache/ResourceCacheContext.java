package com.twilio.oai.java.cache;


public class ResourceCacheContext {
    private static final ThreadLocal<ResourceCache2> CACHE = new ThreadLocal<>();

    public static void set(ResourceCache2 cache) {
        CACHE.set(cache);
    }

    public static ResourceCache2 get() {
        return CACHE.get();
    }

    public static void clear() {
        if (CACHE.get() != null) CACHE.get().clear();
    }
}

