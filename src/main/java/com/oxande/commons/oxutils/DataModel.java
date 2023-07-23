package com.oxande.commons.oxutils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.oxande.commons.oxutils.exception.BusinessException;
import com.oxande.commons.oxutils.mapper.JacksonConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The data model used to respond.
 *
 * @author wrey
 */
public class DataModel implements Map<String, Object> {
    private static final Logger LOG = LoggerFactory.getLogger(DataModel.class);
    public static final int MAX_LIST_SIZE = 1000;

    private final ObjectMapper mapper = JacksonConfiguration.objectMapper();
    private final Map<String, Object> model;
    private Map<String, Object> errorMap = null;
    private final Date started;
    private HttpStatus status = HttpStatus.OK;

    public DataModel() {
        this.model = new HashMap<>();
        this.started = new Date();
    }

    /**
     * Set the response as an error.
     *
     * @param ex the exception
     */
    public void setException(Throwable ex) {
        model.clear();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        errorMap = new HashMap<>();
        errorMap.put("status", status.ordinal());
        errorMap.put("code", ex.getClass().getSimpleName());
        errorMap.put("message", ex.getMessage());
        if (ex instanceof BusinessException) {
            BusinessException businessError = (BusinessException) ex;
            errorMap.put("resolution", businessError.getResolution());
        }
    }


    protected ResponseEntity<String> asResponse(Map<String, Object> map, HttpHeaders headers, HttpStatus code) {
        String output;
        HttpHeaders responseHeaders = Optional.ofNullable(headers).orElse(new HttpHeaders());

        try {
            output = mapper.writeValueAsString(map);
        } catch (JsonProcessingException ex) {
            output = "{error:{\"code\":\"internal\", \"message\": \"Erreur interne de comversion\"}}";
            code = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(output, responseHeaders, code);
    }

    /**
     * Return the data model as a success. This is more or less the only way to
     * return data.
     */
    public ResponseEntity<String> success() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (model.size() == 0) {
            // If no data returned, just add the success
            model.put("success", true);
        }
        return asResponse(model, responseHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> error() {
        HttpHeaders responseHeaders = new HttpHeaders();
        model.clear();
        model.replace("error", errorMap);
        return asResponse(model, responseHeaders, status);
    }

    public ResponseEntity<String> error(String message) {
        return error(message, null);
    }

    public ResponseEntity<String> error(String message, String resolution) {
        errorMap = new HashMap<>();
        errorMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
        errorMap.put("code", "controller");
        errorMap.put("message", message);
        errorMap.put("resolution", resolution);
        return error();
    }

    /**
     * Add a date expressed in milliseconds since Epoch (January 1st, 1970).
     *
     * @param key the key of storage.
     * @param millis the number of milliseconds.
     */
    public void putDate(String key, long millis) {
        Date d = new Date(millis);
        putDate(key, d);
    }

    /**
     * Put the date in the model.
     *
     * @param key the key of the model
     * @param d the date.
     */
    public void putDate(String key, Date d) {
        putObject(key, d);
    }

    /**
     * Put the object in the model.
     *
     * @param key the key
     * @param obj the object
     */
    public void putObject(String key, Object obj) {
        model.put(key, obj);
    }

    public DataModel with(String key, Object obj) {
        putObject(key, obj);
        return this;
    }

    /**
     * Put a collection into the model.
     *
     * @param key the key.
     * @param coll the collection.
     */
    public void putList(String key, Collection<?> coll) {
        List<?> list = new ArrayList<>(coll);
        if (list.size() >= MAX_LIST_SIZE) {
            this.status = HttpStatus.PARTIAL_CONTENT;
        }
        putObject(key, list);
    }

    public void putStream(String key, Stream<?> stream) {
        List<?> list = stream.limit(MAX_LIST_SIZE + 1).collect(Collectors.toList());
        putList(key, list);
    }

    public void putMap(String key, Map<String, ?> map) {
        putObject(key, map);
    }

    public DataModel withMap(String key, Map<String, ?> map) {
        putMap(key, map);
        return this;
    }


    public void putList(String key, Iterable<?> coll, int first, int size) {
        List<?> list = fromStream(StreamSupport.stream(coll.spliterator(), false), first, size);
        putObject(key, list);
    }

    public DataModel withList(String key, Iterable<?> coll, int first, int size) {
        putList(key, coll, first, size);
        return this;
    }

    private List<?> fromStream(Stream<?> stream, int first, int size) {
        return stream //
                .filter(e -> e != null) // Remove null values
                .skip(first).limit(size) // Limit to the size
                .collect(Collectors.toList());
    }

    public void putLong(String key, long value) {
        putObject(key, value);
    }

    public DataModel withLong(String key, long value) {
        putLong(key, value);
        return this;
    }

    public void putInteger(String key, int value) {
        putObject(key, value);
    }

    /**
     * Put any object in the model as a {@link String}.
     *
     * @param key the key.
     * @param obj the object to store.
     */
    public void putString(String key, CharSequence obj) {
        putObject(key, obj == null ? null : obj.toString());
    }

    public DataModel withString(String key, CharSequence obj) {
        putString(key, obj);
        return this;
    }

    public void putDate(String key, Instant instant) {
        putDate(key, instant.toEpochMilli());
    }

    public DataModel withDate(String key, Instant instant) {
        putDate(key, instant);
        return this;
    }

    /**
     * Create a new map in the model.
     *
     * @param key the key of this map.
     * @return the map created.
     */
    public ObjectNode newMap(String key) {
        ObjectNode map = mapper.createObjectNode();
        model.replace(key, map);
        return map;
    }


    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(model);
        } catch (JsonProcessingException ex) {
            return super.toString();
        }
    }

    @Override
    public int size() {
        return model.size();
    }

    @Override
    public boolean isEmpty() {
        return model.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return model.containsKey(key.toString());
    }

    @Override
    public boolean containsValue(Object value) {
        return model.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return model.get(key.toString());
    }

    @Override
    public Object put(String key, Object value) {
        return model.put(key, value);
    }

    public DataModel append(String key, Object value) {
        model.put(key, value);
        return this;
    }

    @Override
    public Object remove(Object key) {
        return model.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        model.putAll(m);
    }

    @Override
    public void clear() {
        model.clear();
    }

    @Override
    public Set<String> keySet() {
        return model.keySet();
    }

    @Override
    public Collection<Object> values() {
        return model.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return model.entrySet();
    }
}
