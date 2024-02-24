package com.nansha.largobjecttransclient.client;

import com.nansha.largobjecttransclient.model.UploadResult;

public class SftpClient {
  private final String name;

  public SftpClient(String name) {
    this.name = name;
  }

  public UploadResult uploadFile(String content, String fileName) {
    // Upload the file to the SFTP server
    System.out.println("Uploading file " + fileName + " to SFTP server " + name + "...");
    return new UploadResult(fileName, true);
  }
}
