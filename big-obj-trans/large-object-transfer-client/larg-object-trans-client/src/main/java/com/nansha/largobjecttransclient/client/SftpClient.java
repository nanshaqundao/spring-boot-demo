package com.nansha.largobjecttransclient.client;

import com.nansha.largobjecttransclient.model.UploadResult;

public class SftpClient {
  private String name;

  public SftpClient(String name) {
    this.name = name;
  }

  public UploadResult uploadFile(String content, String fileName) {
    // Upload the file to the SFTP server
    return new UploadResult(fileName, true);
  }
}
