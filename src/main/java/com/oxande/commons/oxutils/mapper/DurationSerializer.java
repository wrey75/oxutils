package com.oxande.commons.oxutils.mapper;

import java.io.IOException;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@SuppressWarnings("serial")
public class DurationSerializer extends StdSerializer<Duration> {

    public DurationSerializer() {
	this(Duration.class);
    }

    public DurationSerializer(Class<Duration> t) {
	super(t);
    }

    @Override
    public void serialize(Duration duration, JsonGenerator jgen, SerializerProvider provider)
	    throws IOException {
	if (duration == null) {
	    jgen.writeNull();
	} else {
	    // Wite the duration in seconds (with decimals)
	    jgen.writeNumber(duration.toMillis() / 1000.0);
    }
    }
}
