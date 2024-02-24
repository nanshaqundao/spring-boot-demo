package com.nansha.largobjecttransclient.model;

public class UploadResult {
  private String fileName;
  private boolean success;

  public UploadResult(String fileName, boolean b) {
    this.fileName = fileName;
    this.success = b;
  }

  public String getFileName() {
    return fileName;
  }

  public boolean isSuccess() {
    return success;
  }

  @Override
  public String toString() {
    return "UploadResult{" + "fileName='" + fileName + '\'' + ", success=" + success + '}';
  }
}
