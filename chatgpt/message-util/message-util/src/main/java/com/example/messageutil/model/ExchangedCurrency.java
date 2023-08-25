package com.example.messageutil.model;

import java.util.Currency;

public class ExchangedCurrency {
    private Currency currency;
    private Double amount;
    private String settlementInstructions;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSettlementInstructions() {
        return settlementInstructions;
    }

    public void setSettlementInstructions(String settlementInstructions) {
        this.settlementInstructions = settlementInstructions;
    }
}
