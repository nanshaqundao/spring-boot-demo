package com.example.concurrentcallclient.util;

import com.example.concurrentcallclient.model.LargeJsonObject;
import com.example.concurrentcallclient.model.TargetObjectFromLargeObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataUtils {
  public static TargetObjectFromLargeObject processLargeJsonObject(
      LargeJsonObject largeJsonObject) {
    var newData = largeJsonObject.data();
    // encode data with base64
    var encodedData = java.util.Base64.getEncoder().encodeToString(newData.getBytes());
    return new TargetObjectFromLargeObject(
        largeJsonObject.name(), largeJsonObject.data(), encodedData);
  }

  public static TargetObjectFromLargeObject processJson(String json) {
    ObjectMapper objectMapper = new ObjectMapper();
    LargeJsonObject largeObjectData = null;
    try {
      largeObjectData = objectMapper.readValue(json, LargeJsonObject.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return DataUtils.processLargeJsonObject(largeObjectData);
  }
}
