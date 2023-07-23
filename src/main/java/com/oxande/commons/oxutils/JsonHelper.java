package com.oxande.commons.oxutils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JsonHelper {
    private static final Logger LOG = LoggerFactory.getLogger(JsonHelper.class);
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

    private JsonHelper() {
        // No value
    }

    public static int toInt(JsonNode node) {
        return node == null ? 0 : node.asInt();
    }

    public static String toText(JsonNode node) {
        return node == null ? null : node.asText();
    }

    public static SafeMap toMap(String jsonString) {
        try {
            Map<String, Object> map = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>(){});
            return SafeMap.of(map);
        } catch (JsonProcessingException ex) {
            LOG.warn("Can not convert {}", jsonString, ex);
            return null;
        }
    }

    public static List<JsonNode> arrayOf(JsonNode node, String field) {
        List<JsonNode> array = new ArrayList<>();
        if (node != null) {
            if (node.has(field)) {
                JsonNode child = node.get(field);
                child.iterator().forEachRemaining(array::add);
            }
        }
        return array;
    }

    public static JsonNode from(JsonNode node, String longField) {
        JsonNode current = node;
        String fields[] = longField.split("/");
        for (int i = 0; current != null && i < fields.length; i++) {
            current = current.get(fields[i]);
        }
        return current;
    }
}
