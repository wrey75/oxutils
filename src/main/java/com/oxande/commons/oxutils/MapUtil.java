package com.oxande.commons.oxutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MapUtil extends HashMap<String, Object> {

    protected MapUtil() {
        super(10);
    }

    public static MapUtil from() {
        return new MapUtil();
    }

    public static MapUtil from(String key, Object value) {
        return MapUtil.from().with(key, value);
    }

    public final MapUtil with(String key, Object value) {
        put(key, value);
        return this;
    }

    public static <T, V> Map<T, V> init(T[] data, Class<V> aClass) {
        Map<T, V> map = new HashMap<>();
        try {
            Constructor<V> constructor = aClass.getDeclaredConstructor();
            for (T key : data) {
                V value = constructor.newInstance();
                map.put(key, value);
            }
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("No construction for " + aClass, ex);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException("Can not instantiate " + aClass, ex);
        }
        return map;
    }
}
