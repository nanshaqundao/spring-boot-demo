package com.example.businessclientservice.model;

import com.example.businessclientservice.protobuf.BusinessPayload;

public class BusinessPayloadDto {
  private String name;
  private String order;
  private String content;

  // Static factory method to convert from Protobuf
  public static BusinessPayloadDto fromProto(BusinessPayload proto) {
    BusinessPayloadDto dto = new BusinessPayloadDto();
    dto.setName(proto.getName());
    dto.setOrder(proto.getOrder());
    dto.setContent(proto.getContent());
    return dto;
  }

  // Getters and setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
