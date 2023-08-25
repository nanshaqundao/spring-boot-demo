package com.example.messageutil.model;

public class FxSpot {
    private ExchangedCurrency currency1;
    private ExchangedCurrency currency2;
    private ExchangeRate exchangeRate;

    public ExchangedCurrency getCurrency1() {
        return currency1;
    }

    public void setCurrency1(ExchangedCurrency currency1) {
        this.currency1 = currency1;
    }

    public ExchangedCurrency getCurrency2() {
        return currency2;
    }

    public void setCurrency2(ExchangedCurrency currency2) {
        this.currency2 = currency2;
    }

    public ExchangeRate getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(ExchangeRate exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
