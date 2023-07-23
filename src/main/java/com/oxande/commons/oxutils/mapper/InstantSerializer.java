package com.oxande.commons.oxutils.mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@SuppressWarnings("serial")
public class InstantSerializer extends StdSerializer<Instant> {

    public InstantSerializer() {
	this(Instant.class);
    }

    public InstantSerializer(Class<Instant> t) {
	super(t);
    }

    @Override
    public void serialize(Instant instant, JsonGenerator jgen, SerializerProvider provider)
	    throws IOException {
	if (instant == null) {
	    jgen.writeNull();
	} else {
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	    long v = instant.toEpochMilli();
	    Date d = new Date(v);
	    jgen.writeString(formatter.format(d));
	}
    }
}
