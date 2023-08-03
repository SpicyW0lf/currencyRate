package DAO;

import mappers.ResultSetMapper;
import models.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO implements BaseDAO<Currency> {

    public static final CurrencyDAO INSTANCE = new CurrencyDAO();
    private final String url = "jdbc:sqlite::resource:currdb";//"jdbc:sqlite:" + System.getProperty("user.home") + "/currdb";

    private CurrencyDAO() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Currency> findAll() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url)) {
            List<Currency> currencies = new ArrayList<>();
            Statement stat = conn.createStatement();

            stat.execute("SELECT * FROM Currencies");
            ResultSet rs = stat.getResultSet();
            while (rs.next()) {
                currencies.add(ResultSetMapper.mapToCurrency(rs));
            }

            return currencies;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public Currency findByCode(String code) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM Currencies WHERE code = ?");
            stat.setString(1, code);
            stat.execute();
            ResultSet rs = stat.getResultSet();

            if (rs.next()) {
                return ResultSetMapper.mapToCurrency(rs);
            } else return null;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public Currency findById(int id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM Currencies WHERE id = ?");
            stat.setInt(1, id);
            stat.execute();
            ResultSet rs = stat.getResultSet();

            if (rs.next()) {
                return ResultSetMapper.mapToCurrency(rs);
            } else return null;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public void create(Currency currency) throws SQLException {
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
