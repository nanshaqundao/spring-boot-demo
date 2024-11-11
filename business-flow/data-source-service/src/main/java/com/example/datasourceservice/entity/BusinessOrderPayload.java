package com.example.datasourceservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "business_order_payload")
public class BusinessOrderPayload {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column private String orderNumber;

  @Column private String content;

  public BusinessOrderPayload() {}

  public BusinessOrderPayload(String name, String orderNumber, String content) {
    this.name = name;
    this.orderNumber = orderNumber;
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
