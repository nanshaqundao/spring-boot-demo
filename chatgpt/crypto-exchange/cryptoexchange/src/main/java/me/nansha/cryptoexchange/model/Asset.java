package me.nansha.cryptoexchange.model;

public class Asset {
    private String name;

    public Asset() {
    }

    public Asset(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
