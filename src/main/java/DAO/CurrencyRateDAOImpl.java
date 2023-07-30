package DAO;

import mappers.CurrencyRateMapper;
import models.CurrencyRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRateDAOImpl implements CurrencyRateDAO {

    public static final CurrencyRateDAOImpl INSTANCE = new CurrencyRateDAOImpl();
    private final String url = "jdbc:sqlite:C:\\projects\\currencyRate\\currdb";

    private CurrencyRateDAOImpl(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CurrencyRate> getAllCurrencyRates() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url)) {
            List<CurrencyRate> currencyRates = new ArrayList<>();
            Statement stat = conn.createStatement();
            stat.execute("SELECT * FROM ExchangeRates");
            ResultSet rs = stat.getResultSet();

            while (rs.next()) {
                currencyRates.add(CurrencyRateMapper.mapFromResultSet(rs));
            }

            return currencyRates;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public CurrencyRate getCurrencyRateByCodes(String code) throws SQLException {
        return null;
    }

    @Override
    public void createCurrencyRate(CurrencyRate currencyRate) throws SQLException {

    }
}
