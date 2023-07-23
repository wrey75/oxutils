package com.oxande.commons.oxutils.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

public class ObjectIdSerializerTest {

    public static class SimpleObject {
        private ObjectId id;
        private String value = "42";

        SimpleObject(String s) {
            if (s != null) this.id = new ObjectId(s);
        }


        public ObjectId getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
    }

    @Test
    public void testSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ObjectIdSerializer());
        mapper.registerModule(module);

        SimpleObject obj0 = new SimpleObject("258974363512345871256987");
        String serialized0 = mapper.writeValueAsString(obj0);
        Assert.assertTrue(serialized0.contains("\"258974363512345871256987\""));

        SimpleObject obj1 = new SimpleObject(null);
        String serialized1 = mapper.writeValueAsString(obj1);
        Assert.assertTrue(serialized1.contains("\"id\":null"));
    }
}
