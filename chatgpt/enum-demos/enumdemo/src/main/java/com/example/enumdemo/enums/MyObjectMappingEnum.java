package com.example.enumdemo.enums;

import java.util.List;

import static com.example.enumdemo.enums.ApplicableJurisdiction.UK;
import static com.example.enumdemo.enums.ApplicableJurisdiction.USA;

public enum MyObjectMappingEnum {
  CurrentDate("currentDate", "top.lv1.currentDate", FieldType.SingleField, List.of(USA, UK)),
  OtherParty("otherParty", "top.lv2.otherParty", FieldType.SingleField, List.of(USA)),
  FinalAmount("finalAmount", "top.lv2.finalAmount", FieldType.ArrayField, List.of(UK));


  private final String name;
  private final String objectPath;
  private final FieldType fieldType;
  private final List<ApplicableJurisdiction> applicableJurisdictions;

  MyObjectMappingEnum(
      String name,
      String objectPath,
      FieldType fieldType,
      List<ApplicableJurisdiction> applicableJurisdictions) {
    this.name = name;
    this.objectPath = objectPath;
    this.fieldType = fieldType;
    this.applicableJurisdictions = applicableJurisdictions;
  }
}
