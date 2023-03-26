package com.example.mcdemo;

import lombok.Data;

@Data
public class SurveyData {
    private String receiptCode;
    private double spentAmount;
    private String email;

    // compatible maven dependency of selenium
}
