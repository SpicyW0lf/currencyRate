# Обмен валют

## __Стэк__:
Jakarta Servlets, Maven, Jackson, JDBC, SQLite,

## __Описание:__

REST API серивис для выполнения CRUD операций над валютами и их курсами,
а также для конвертации произвольного количества одной валюты в другую по прямому,
обратному курсу или кросс курсу через USD.

## __API:__

### __Валюты__

#### __GET `/currencies`__

Получение списка всех валют. Пример ответа:

```json
[
    "currency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },   
    "currency": {
        "id": 0,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    }
]
```

#### __GET `/currency/USD`__

Получение конкретной валюты. Пример ответа:


```json
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

#### __POST `/currencies/new`__

Добавление новой валюты в базу. Данные передаются в теле запроса
в виде полей формы (multipart/form-data).
Поля формы - name, code, sign. 
Пример ответа - JSON представление вставленной в базу записи, включая её ID:

```json
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

### Обменные курсы

#### __GET `/exchangeRates`__

Получение списка всех обменных курсов. Пример ответа:

```json
[
    "id": 0,
    "exchangeRate": {
        "baseCurrency": {
            "id": 0,
            "name": "United States dollar",
            "code": "USD",
            "sign": "$"
        },
        "targetCurrency": {
            "id": 1,
            "name": "Euro",
            "code": "EUR",
            "sign": "€"
        },
        "rate": 0.99
    }
]
```

#### __GET `/exchangeRate/USDRUB`__

Получение конкретного обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса. Пример ответа:

```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

#### __POST__ `/exchangeRates/new`__

Добавление нового обменного курса в базу. Данные передаются в теле запроса в виде полей формы (`multipart/form-data`). Поля формы - `baseCurrencyCode`, `targetCurrencyCode`, `rate`. Пример полей формы:
    
* `baseCurrencyCode` - USD
* `targetCurrencyCode` - EUR
* `rate` - 0.99

Пример ответа - JSON представление вставленной в базу записи, включая её ID:

```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

#### __PATCH `/exchangeRates/update/USDRUB`__

Обновление существующего в базе обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса. Данные передаются в теле запроса в виде полей формы (`multipart/form-data`). Единственное поле формы - `rate`.

Пример ответа - JSON представление обновлённой записи в базе данных, включая её ID:

```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

### Обмен валюты

#### __GET `/exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT`__

Рассчёт перевода определённого количества средств из одной валюты в другую. Пример запроса - GET `/exchange?from=USD&to=AUD&amount=10`.

Пример ответа:

```json
{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "A€"
    },
    "rate": 1.45,
    "amount": 10.00
    "convertedAmount": 14.50
}
```

---

Для всех запросов, в случае ошибки, ответ будет выглядеть так:

```json
{
    "message": "Валюта не найдена"
}
```
