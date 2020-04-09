package com.initsysctrl.omnidemo.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.net.URL;

public class EhcacheUtil {
    private static final String path = "/ehcache.xml";

    private URL url;

    private CacheManager manager;
    private static EhcacheUtil ehCache;

    private EhcacheUtil(String path) {
        url = getClass().getResource(path);
        manager = CacheManager.create(url);
    }

    public static EhcacheUtil getInstance() {
        if (ehCache == null) {
            ehCache = new EhcacheUtil(path);
        }
        return ehCache;
    }

    public void put(String cacheName, String key, Object value) {
        System.out.println("putput");
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
        cache.flush();
    }

    public Object get(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    public Cache get(String cacheName) {
        return manager.getCache(cacheName);
    }

    public void remove(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        cache.remove(key);
        cache.flush();
    }
    public void removeAll(String cacheName) {
        Cache cache = manager.getCache(cacheName);
        cache.removeAll();
        cache.flush();
    }


}
