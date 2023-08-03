package mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }
}
