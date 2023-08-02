package mappers;

import DAO.CurrencyDAO;
import models.Currency;
import models.CurrencyRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetMapper {

    public static Currency mapToCurrency(ResultSet rs) throws SQLException {
        return new Currency(
                rs.getInt("id"),
                rs.getString("Code"),
                rs.getString("FullName"),
                rs.getString("Sign")
        );
    }

    public static CurrencyRate mapToCurrencyRate(ResultSet rs) throws SQLException {
        CurrencyDAO currencyDAO = CurrencyDAO.INSTANCE;

        return new CurrencyRate(
                rs.getInt("id"),
                currencyDAO.findById(rs.getInt("BaseCurrencyId")),
                currencyDAO.findById(rs.getInt("TargetCurrencyId")),
                rs.getBigDecimal("rate")
        );
    }
}
