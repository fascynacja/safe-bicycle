package org.pysz.safebicycle.dto.response;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class AmountSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeString(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }
}
