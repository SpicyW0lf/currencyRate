package DAO;

import mappers.CurrencyRateMapper;
import models.Currency;
import models.CurrencyRate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRateDAOImpl implements CurrencyRateDAO {

    public static final CurrencyRateDAOImpl INSTANCE = new CurrencyRateDAOImpl();
    private final CurrencyDAOImpl currencyDAO = CurrencyDAOImpl.INSTANCE;
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
        String base = code.substring(0, 3);
        String target = code.substring(3);
        Currency baseCurr = currencyDAO.getCurrencyByCode(base);
        Currency targetCurr = currencyDAO.getCurrencyByCode(target);

        if (baseCurr == null || targetCurr == null) {
            return null;
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ExchangeRates" +
                    " WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?");
            ps.setInt(1, baseCurr.id());
            ps.setInt(2, targetCurr.id());
            ps.execute();
            ResultSet rs = ps.getResultSet();

            if (!rs.next()) {
                return null;
            }
            return CurrencyRateMapper.mapFromResultSet(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateCurrencyRate(CurrencyRate currencyRate, BigDecimal rate) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE ExchangeRates SET rate = ? WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?"
            );
            ps.setBigDecimal(1, rate);
            ps.setInt(2, currencyRate.baseCurrency().id());
            ps.setInt(3, currencyRate.targetCurrency().id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void createCurrencyRate(CurrencyRate currencyRate) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ExchangeRates" +
                    " VALUES (null, ?, ?, ?)");
            ps.setInt(1, currencyRate.baseCurrency().id());
            ps.setInt(2, currencyRate.targetCurrency().id());
            ps.setBigDecimal(3, currencyRate.rate());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
