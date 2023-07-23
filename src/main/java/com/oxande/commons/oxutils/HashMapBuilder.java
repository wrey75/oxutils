package com.oxande.commons.oxutils;

import java.util.HashMap;
import java.util.Map;

public class HashMapBuilder {

    private final Map<String, Object> values = new HashMap<>();

    public HashMapBuilder add(String key, Object obj) {
        if (obj != null) {
            values.put(key, obj);
        }
        return this;
    }

    public HashMapBuilder put(String key, Object obj) {
        values.put(key, obj);
        return this;
    }

    public Map<String, Object> build() {
        return values;
    }
}
