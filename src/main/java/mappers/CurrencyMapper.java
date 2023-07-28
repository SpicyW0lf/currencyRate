package mappers;

import models.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyMapper {
    public static Currency mapFromResultSet(ResultSet rs) throws SQLException {
        return new Currency(
                rs.getInt("id"),
                rs.getString("Code"),
                rs.getString("FullName"),
                rs.getString("Sign")
        );
    }
}
