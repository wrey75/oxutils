package com.oxande.commons.oxutils.mapper;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Serialize simply the day (not the time). Used for day values.
 * 
 * @author wrey
 *
 */
@SuppressWarnings("serial")
public class LocalDateSerializer extends StdSerializer<LocalDate> {

    public LocalDateSerializer() {
	this(LocalDate.class);
    }

    public LocalDateSerializer(Class<LocalDate> t) {
	super(t);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider)
	    throws IOException {
	if (value == null) {
	    jgen.writeNull();
	} else {
	    String formatted = String.format("%04d-%02d-%02d", value.getYear(), value.getMonthValue(), value.getDayOfMonth());
	    jgen.writeString(formatted);
	}
    }
}
