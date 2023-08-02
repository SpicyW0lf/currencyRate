package services;

import DAO.CurrencyDAO;
import models.Currency;
import util.Validator;

import java.sql.SQLException;
import java.util.List;

public class CurrencyService {

    public static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDAO currencyDAO = CurrencyDAO.INSTANCE;

    private CurrencyService(){};

    public List<Currency> getAll() throws SQLException {
        return currencyDAO.findAll();
    }

    public Currency getByCode(String code) throws SQLException {
        if (!Validator.isCodeValid(code)) {
            return null;
        }

        return currencyDAO.findByCode(code);
    }

    public void create(String code, String name, String sign) throws SQLException {
        currencyDAO.create(new Currency(0, code, name, sign));
    }
}
