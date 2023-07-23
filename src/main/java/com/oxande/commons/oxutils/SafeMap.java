package com.oxande.commons.oxutils;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class SafeMap extends HashMap<String, Object> {
    private static final Logger LOG = LoggerFactory.getLogger(SafeMap.class);

    public static SafeMap of(Map<String, Object> map) {
        return new SafeMap(map);
    }

    public static SafeMap empty() {
        return of(new HashMap<>());
    }

    public static SafeMap with(String key, Object value) {
        return empty().and(key, value);
    }

    public SafeMap and(String key, Object value) {
        this.put(key, value);
        return this;
    }

    private SafeMap() {
        super();
    }

    private SafeMap(Map<String, Object> map) {
        super(map);
    }

    public String getString(String key) {
        Object val = get(key);
        return (val == null ? null : StringUtils.trimToNull(val.toString()));
    }

    public Date getDate(String key) {
        Object val = get(key);
        if (val == null) return null;
        if (val instanceof Date) return (Date) val;
        return DateUtil.parse(val.toString());
    }

    @SuppressWarnings("java:S2447")
    public Boolean getBoolean(String key) {
        Object val = get(key);
        if (val == null) return null;
        if (val instanceof Boolean) return (Boolean) val;
        if (val instanceof Number) return ((Number) val).intValue() != 0;
        return BooleanUtils.toBooleanObject(val.toString());
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Optional.ofNullable(getBoolean(key)).orElse(defaultValue);
    }

    public SafeMap getMap(String key) {
        Object val = get(key);
        return (val instanceof Map ? SafeMap.of((Map<String, Object>) val) : SafeMap.empty());
    }

    public URL getUrl(String key) {
        String urlstring = getString(key);
        if (urlstring != null) {
            try {
                return new URL(urlstring);
            } catch (MalformedURLException e) {
                LOG.warn("URL {} is not valid.", key);
            }
        }
        return null;
    }

    public List<SafeMap> getList(String key) {
        Object val = get(key);
        if (val instanceof Collection) {
            List<SafeMap> list = new ArrayList<>();
            for (Object item : (Collection<?>) val) {
                if (item instanceof Map) {
                    list.add(SafeMap.of((Map) item));
                } else {
                    SafeMap singleMap = new SafeMap();
                    singleMap.put("_", item);
                    list.add(singleMap);
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    public double getDouble(String key, double defaultValue) {
        String str = getString(key);
        return NumberUtils.toDouble(str, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        String str = getString(key);
        return NumberUtils.toFloat(str, defaultValue);
    }

    public static Map<String, Serializable> serialized(Map<String, Object> source) {
        Map<String, Serializable> dest = new HashMap<>();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = StringUtil.slug(entry.getKey());
            if (entry.getValue() != null) {
                if (entry.getValue() instanceof Serializable) {
                    dest.put(key, (Serializable) entry.getValue());
                } else if (entry.getValue() instanceof Map) {
                    dest.putAll(serialized((Map<String, Object>) entry.getValue()));
                } else {
                    // Covert the value to a String
                    dest.put(key, entry.getValue().toString());
                }
            }
        }
        return dest;
    }
}
