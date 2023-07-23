package com.oxande.commons.oxutils;

import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class EasyMap<K, V> extends LinkedHashMap<K, V> {

    public EasyMap<K,V> with(K key, V value) {
        this.put(key, value);
        return this;
    }

    public static <K,V> EasyMap<K, V> empty() {
        return new EasyMap<>();
    }

    public static <K, V> EasyMap of(K key, V value) {
        return new EasyMap<K, V>().with(key, value);
    }
}
