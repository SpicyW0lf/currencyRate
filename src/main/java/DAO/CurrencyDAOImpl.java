package DAO;

import mappers.CurrencyMapper;
import models.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAOImpl implements CurrencyDAO {

    public static final CurrencyDAOImpl INSTANCE = new CurrencyDAOImpl();
    private final String url = "jdbc:sqlite:C:\\projects\\currencyRate\\currdb";

    private CurrencyDAOImpl() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Currency> getAllCurrencies() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url)) {
            List<Currency> currencies = new ArrayList<>();
            Statement stat = conn.createStatement();

            stat.execute("SELECT * FROM Currencies");
            ResultSet rs = stat.getResultSet();
            while (rs.next()) {
                currencies.add(CurrencyMapper.mapFromResultSet(rs));
            }

            return currencies;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public Currency getCurrencyByCode(String code) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM Currencies WHERE code = ?");
            stat.setString(1, code);
            stat.execute();
            ResultSet rs = stat.getResultSet();

            if (rs.next()) {
                return CurrencyMapper.mapFromResultSet(rs);
            } else return null;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public void createCurrency(Currency currency) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Currencies VALUES (null, ?, ?, ?)");
            ps.setString(1, currency.code());
            ps.setString(2, currency.fullName());
            ps.setString(3, currency.sign());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
