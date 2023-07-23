package com.oxande.commons.oxutils.mapper;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.TimeZone;

/**
 * Configure JACKSON mapping into Spring Boot. 
 *  
 *  @see <a href="https://stackoverflow.com/questions/7854030/configuring-objectmapper-in-spring">StackOverflow</a>
 *  
 *  @author wrey
 *
 */
public class JacksonConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(JacksonConfiguration.class);
    private static final JacksonConfiguration instance = new JacksonConfiguration();
    
    private ObjectMapper mapper;
    
    private JacksonConfiguration() {
        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        mapper = new ObjectMapper();
        
        SimpleModule simpleModule = new SimpleModule("O240Module", new Version(1,0,0,null, null, null));
        simpleModule.addSerializer(ObjectId.class, new ObjectIdSerializer());
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        simpleModule.addSerializer(Instant.class, new InstantSerializer());
        simpleModule.addSerializer(Duration.class, new DurationSerializer());
        simpleModule.addSerializer(BigDecimal.class, new MoneySerializer());
        mapper.registerModule(simpleModule);

        // Use local timezone for mapping.
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getDefault());
        mapper.setDateFormat(dateFormat);
        
        // mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
    }
    
    public static JacksonConfiguration getInstance() {
        return instance;
    }
    
    public static ObjectMapper objectMapper() {
        return instance.mapper;
    }

    public static <T> T readValue(String s, Class<T> clazz) {
        try {
            return objectMapper().readValue(s, clazz);
        } catch (IOException e) {
            LOG.error("Can not convert '{}'", s, e);
        }
        return null;
    }
    
}
