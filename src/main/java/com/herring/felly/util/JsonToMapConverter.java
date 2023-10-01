package com.herring.felly.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JsonToMapConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> convertJsonToMap(String jsonString) {
        try {
            if (!jsonString.isEmpty()) {
                return objectMapper.readValue(jsonString, Map.class);
            } else {
                return new HashMap<>();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
