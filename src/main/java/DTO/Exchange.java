package DTO;

import models.Currency;

import java.math.BigDecimal;

public record Exchange(
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount
) {}
