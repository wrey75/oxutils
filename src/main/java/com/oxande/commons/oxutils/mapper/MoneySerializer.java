package com.oxande.commons.oxutils.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class MoneySerializer extends StdSerializer<BigDecimal> {

    public MoneySerializer() {
        this(BigDecimal.class);
    }

    public MoneySerializer(Class<BigDecimal> t) {
        super(t);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value == null) {
            jgen.writeNull();
        } else {
            // Write the decimals
            jgen.writeRawValue(value.toPlainString());
        }
    }
}
