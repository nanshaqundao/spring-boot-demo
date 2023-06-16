package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class RefResponse {
    private String srcCode;
    private String annaCode;
    private String crossCode;

    private List<Payload> payloadList;

    public String getCrossCode() {
        return crossCode;
    }

    public void setCrossCode(String crossCode) {
        this.crossCode = crossCode;
    }

    public String getSrcCode() {
        return srcCode;
    }

    public void setSrcCode(String srcCode) {
        this.srcCode = srcCode;
    }

    public String getAnnaCode() {
        return annaCode;
    }

    public void setAnnaCode(String annaCode) {
        this.annaCode = annaCode;
    }

    public List<Payload> getPayloadList() {
        return payloadList;
    }

    public void setPayloadList(List<Payload> payloadList) {
        this.payloadList = payloadList;
    }
}
