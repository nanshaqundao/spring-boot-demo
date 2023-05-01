package model;

public class Payload {
    private String notionalCurrencyOne;
    private String notionalCurrencyTwo;
    private String specialCode;

    public String getNotionalCurrencyOne() {
        return notionalCurrencyOne;
    }

    public void setNotionalCurrencyOne(String notionalCurrencyOne) {
        this.notionalCurrencyOne = notionalCurrencyOne;
    }

    public String getNotionalCurrencyTwo() {
        return notionalCurrencyTwo;
    }

    public void setNotionalCurrencyTwo(String notionalCurrencyTwo) {
        this.notionalCurrencyTwo = notionalCurrencyTwo;
    }

    public String getSpecialCode() {
        return specialCode;
    }

    public void setSpecialCode(String specialCode) {
        this.specialCode = specialCode;
    }
}
