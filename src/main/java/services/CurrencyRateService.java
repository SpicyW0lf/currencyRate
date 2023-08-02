package services;

import DAO.CurrencyRateDAO;
import models.Currency;
import models.CurrencyRate;
import util.Validator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CurrencyRateService {

    public static final CurrencyRateService INSTANCE = new CurrencyRateService();

    private CurrencyRateService(){}

    private final CurrencyRateDAO currencyRateDAO = CurrencyRateDAO.INSTANCE;

    public List<CurrencyRate> getAll() throws SQLException {
        return currencyRateDAO.findAll();
    }

    public CurrencyRate getByCodePair(String codePair) throws SQLException {
        if (!Validator.isCodePairValid(codePair)) {
            return null;
        }

        return currencyRateDAO.findByCode(codePair);
    }


    public void create(String baseCurrencyCode, String targetCurrencyCode, String rate) throws SQLException {
        CurrencyService currencyService = CurrencyService.INSTANCE;
        Currency base = currencyService.getByCode(baseCurrencyCode);
        Currency target = currencyService.getByCode(targetCurrencyCode);

        currencyRateDAO.create(new CurrencyRate(
                0,
                base,
                target,
                BigDecimal.valueOf(Double.parseDouble(rate))
        ));
    }
}
