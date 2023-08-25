package com.example.demo.model;

public class Payload {
private String name;
    private String summary;
    private String extraData;

    public Payload() {}

    public Payload(String name, String summary, String extraData) {
        this.name = name;
        this.summary = summary;
        this.extraData = extraData;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
}
