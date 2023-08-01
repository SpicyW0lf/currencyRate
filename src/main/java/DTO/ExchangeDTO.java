package DTO;

import models.Currency;

import java.math.BigDecimal;

public record ExchangeDTO(
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount
) {}
