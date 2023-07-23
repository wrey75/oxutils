package com.oxande.commons.oxutils.mapper;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@SuppressWarnings("serial")
public class ObjectIdSerializer extends StdSerializer<ObjectId> {

	public ObjectIdSerializer() {
		this(ObjectId.class);
	}

	public ObjectIdSerializer(Class<ObjectId> t) {
		super(t);
	}

	@Override
	public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException {
		if (value == null) {
			jgen.writeNull();
		} else {
			jgen.writeString(value.toHexString());
		}
	}
}
