package DAO;

import mappers.ResultSetMapper;
import models.Currency;
import models.CurrencyRate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRateDAO implements BaseDAO<CurrencyRate> {

    public static final CurrencyRateDAO INSTANCE = new CurrencyRateDAO();
    private final CurrencyDAO currencyDAO = CurrencyDAO.INSTANCE;
    private final String url = "jdbc:sqlite::resource:currdb";

    private CurrencyRateDAO(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CurrencyRate> findAll() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url)) {
            List<CurrencyRate> currencyRates = new ArrayList<>();
            Statement stat = conn.createStatement();
            stat.execute("SELECT * FROM ExchangeRates");
            ResultSet rs = stat.getResultSet();

            while (rs.next()) {
                currencyRates.add(ResultSetMapper.mapToCurrencyRate(rs));
            }

            return currencyRates;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public CurrencyRate findByCode(String code) throws SQLException {
        String base = code.substring(0, 3);
        String target = code.substring(3);
        Currency baseCurr = currencyDAO.findByCode(base);
        Currency targetCurr = currencyDAO.findByCode(target);

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
            return ResultSetMapper.mapToCurrencyRate(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void update(CurrencyRate currencyRate, BigDecimal rate) throws SQLException {
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
    public void create(CurrencyRate currencyRate) throws SQLException {
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
