create table ExchangeRates
(
    ID               integer
        constraint ExchangeRates_pk
            primary key autoincrement,
    BaseCurrencyId   integer    not null
        constraint ExchangeRates_Currencies_ID_fk
            references Currencies,
    TargetCurrencyId integer    not null
        constraint ExchangeRates_Currencies_ID_fk2
            references Currencies,
    Rate             Decimal(6) not null,
    constraint ExchangeRates_pk2
        unique (TargetCurrencyId, BaseCurrencyId)
);

