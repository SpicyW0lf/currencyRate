package mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import models.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Currency mapFromResultSet(ResultSet rs) throws SQLException {
        return new Currency(
                rs.getInt("id"),
                rs.getString("Code"),
                rs.getString("FullName"),
                rs.getString("Sign")
        );
    }

    public static String mapToJson(Object curr) throws JsonProcessingException {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(curr);
    }
}
