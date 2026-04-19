package com.parceiroferramentas.api.parceiro_api.serializers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateTimeOffsetDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        return OffsetDateTime.parse(date).toLocalDateTime();
    }
}
