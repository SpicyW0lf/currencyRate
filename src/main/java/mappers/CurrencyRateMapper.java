package mappers;

import DAO.CurrencyDAOImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import models.CurrencyRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyRateMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static CurrencyDAOImpl currencyDAO = CurrencyDAOImpl.INSTANCE;

    public static CurrencyRate mapFromResultSet(ResultSet rs) throws SQLException {
        return new CurrencyRate(
                rs.getInt("id"),
                currencyDAO.getCurrencyById(rs.getInt("BaseCurrencyId")),
                currencyDAO.getCurrencyById(rs.getInt("TargetCurrencyId")),
                rs.getBigDecimal("rate")
        );
    }

    public static String mapToJson(Object curr) throws JsonProcessingException {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(curr);
    }
}
