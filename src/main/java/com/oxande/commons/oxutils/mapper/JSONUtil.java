package com.oxande.commons.oxutils.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public final class JSONUtil {
    private static Logger LOG = LoggerFactory.getLogger(JSONUtil.class);
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<Map<String, Object>>() {
    };
    private static final TypeReference<List<Object>> LIST_TYPE = new TypeReference<List<Object>>() {
    };
    private static final ObjectMapper mapper = mapper();

    public static String toJson(Object o) {
        if (o == null) return "<null>";
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            LOG.warn("Can not convert {}", o);
            return o.toString();
        }
    }

    /**
     * Return a complete mapper.
     *
     * @return a new mapper.
     */
    public static ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ObjectIdSerializer());
        mapper.registerModule(module);
        return mapper;
    }

    public static Map<String, Object> jsonAsMap(String json) {
        try {
            return jsonAsMap(mapper.readTree(json));
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(json + ": JSON malformed", ex);
        }
    }

    public static Map<String, Object> jsonAsMap(JsonNode json) {
        return mapper.convertValue(json, MAP_TYPE);
    }

    public static Map<String, Object> pojo2map(Object object) {
        JsonNode node = mapper.valueToTree(object);
        return mapper.convertValue(node, MAP_TYPE);
    }


    public static <T> T map2pojo(Map<String, Object> map, Class<T> aClass) {
        return mapper.convertValue(map, aClass);
    }

    public static List<Object> pojoAsList(Object object) {
        JsonNode node = mapper.valueToTree(object);
        return mapper.convertValue(node, LIST_TYPE);
    }
}
