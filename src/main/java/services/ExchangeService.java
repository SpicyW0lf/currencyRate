package services;

import DAO.CurrencyRateDAO;
import DTO.Exchange;
import models.CurrencyRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class ExchangeService {
    public static final ExchangeService INSTANCE = new ExchangeService();
    private final CurrencyRateDAO currencyRateDAO = CurrencyRateDAO.INSTANCE;

    private ExchangeService(){}

    public Exchange exchangeCurrency(String from, String to, BigDecimal amount) throws SQLException {
        CurrencyRate cr1 = currencyRateDAO.findByCode(from + to);
        if (cr1 != null) {
            return new Exchange(
                    cr1.baseCurrency(),
                    cr1.targetCurrency(),
                    cr1.rate(),
                    amount,
                    amount.multiply(cr1.rate())
            );
        }

        CurrencyRate cr2 = currencyRateDAO.findByCode(to + from);
        if (cr2 != null) {
            return new Exchange(
                    cr2.targetCurrency(),
                    cr2.baseCurrency(),
                    amount.divide(amount.multiply(cr2.rate())),
                    amount,
                    amount.divide(cr2.rate())
            );
        }

        cr1 = currencyRateDAO.findByCode("USD" + from);
        cr2 = currencyRateDAO.findByCode("USD" + to);
        if (cr1 == null || cr2 == null) {
            return null;
        }

        return new Exchange(
                cr1.targetCurrency(),
                cr2.targetCurrency(),
                cr2.rate().divide(cr1.rate(), 2, RoundingMode.DOWN),
                amount,
                amount.multiply(cr2.rate().divide(cr1.rate(), 2, RoundingMode.DOWN))
        );
    }
}
