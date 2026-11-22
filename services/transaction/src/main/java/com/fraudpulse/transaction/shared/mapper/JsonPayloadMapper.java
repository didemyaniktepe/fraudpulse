package com.fraudpulse.transaction.shared.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class JsonPayloadMapper {
    private final ObjectMapper objectMapper;

    @Named("toJson")
    public <T> JsonNode toJson(T value) {
        return objectMapper.valueToTree(value);
    }
}
