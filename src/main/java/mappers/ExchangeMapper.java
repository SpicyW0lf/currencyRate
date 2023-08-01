package mappers;

import DTO.ExchangeDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import models.Currency;

import java.math.BigDecimal;

public class ExchangeMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Currency base, Currency to, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) throws JsonProcessingException {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(new ExchangeDTO(
                base,
                to,
                rate,
                amount,
                convertedAmount
        ));
    }
}
