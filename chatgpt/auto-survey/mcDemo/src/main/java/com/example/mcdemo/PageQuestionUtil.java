package com.example.mcdemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PageQuestionUtil {
    private PageQuestionUtil() {
    }

    public static boolean isPageHavingQuestionText(WebDriver driver, String questionText) {
        String bodyText = driver.findElement(By.tagName("body")).getText();
        return bodyText.contains(questionText);
    }
}
