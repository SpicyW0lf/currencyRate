package DAO;

import models.Currency;
import models.CurrencyRate;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyRateDAO {
    public List<CurrencyRate> getAllCurrencyRates() throws SQLException;
    public CurrencyRate getCurrencyRateByCodes(String code) throws SQLException;
    public void createCurrencyRate(CurrencyRate currencyRate) throws SQLException;
}
