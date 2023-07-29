package DAO;

import models.Currency;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyDAO {
    public List<Currency> getAllCurrencies() throws SQLException;
    public Currency getCurrencyByCode(String code) throws SQLException;
    public void createCurrency(Currency currency) throws SQLException;
}
