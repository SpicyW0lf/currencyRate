package models;

import java.math.BigDecimal;

public record CurrencyRate(
        int id,
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate
) {
}
