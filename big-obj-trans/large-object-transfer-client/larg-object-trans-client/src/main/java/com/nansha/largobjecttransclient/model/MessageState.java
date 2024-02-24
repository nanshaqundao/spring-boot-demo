package com.nansha.largobjecttransclient.model;

public class MessageState {
  private String fieldA;
  private String fieldB;
  private String fieldC;
  private String fieldD;

  public String getFieldA() {
    return fieldA;
  }

  public void setFieldA(String fieldA) {
    this.fieldA = fieldA;
  }

  public String getFieldB() {
    return fieldB;
  }

  public void setFieldB(String fieldB) {
    this.fieldB = fieldB;
  }

  public String getFieldC() {
    return fieldC;
  }

  public void setFieldC(String fieldC) {
    this.fieldC = fieldC;
  }

  public String getFieldD() {
    return fieldD;
  }

  public void setFieldD(String fieldD) {
    this.fieldD = fieldD;
  }

  @Override
  public String toString() {
    return "MessageState{"
        + "fieldA='"
        + fieldA
        + '\''
        + ", fieldB='"
        + fieldB
        + '\''
        + ", fieldC='"
        + fieldC
        + '\''
        + ", fieldD='"
        + fieldD
        + '\''
        + '}';
  }
}
