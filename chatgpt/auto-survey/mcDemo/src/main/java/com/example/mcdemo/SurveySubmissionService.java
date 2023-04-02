package com.example.mcdemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SurveySubmissionService {

    // TODO: implement this service
    // 1. create a new survey data object
    // 2. save the survey data object to the database
    // 3. return the survey data object

    // service should be called from the controller
    // controller should return the survey data object to the user

    // survey data object should have the following fields: SurveyData.java
    // receiptCode
    // spentAmount
    // email

    // survey data are for mcdonalds uk
    // receipt code is the 12 alphabet code on the receipt
    // spent amount is the amount spent on the receipt
    // email is the email of the user
    public void submitSurvey(SurveyData surveyData) {
        // Set the path to your ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\temp\\chromedriver.exe");
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.allowedIps", "");
        // Configure headless mode for Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--allowed-ips=*");
        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--headless");

        // Initialize the WebDriver
        WebDriver driver = new ChromeDriver(options);

        try {
            // Open the survey website
            driver.get("https://www.mcdfoodforthoughts.com/");

            // welcome page click next button
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Welcome to this survey")) {
                // Click the "Next" button
                WebElement welcomeNextButton = driver.findElement(By.id("NextButton"));
                welcomeNextButton.click();
            }


            // do you have the receipt available
            // # Select the "Yes" radio button for question 1
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Do you have your receipt available?")) {
                // Click the "Yes" radio button
                WebElement yes = driver.findElement(By.xpath("//*[@id='surveyQuestions']/fieldset/div/div[1]/span/label"));
                yes.click();

                // Click the "Next" button
                WebElement next_button = driver.findElement(By.id("NextButton"));
                next_button.click();
            }


            // to fill in the 3 text fields of 12 digit code on top fo the receipt
            // to fill and amount spent on the receipt
            // to fill in the email address

            // Input the 12 digit code
            // 12 digit code on the top of your receipt:
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "12 digit code on the top of your receipt:")) {
                String[] codes = surveyData.getReceiptCode().split("-");
                WebElement codeInput1 = driver.findElement(By.id("CN1"));
                codeInput1.sendKeys(codes[0]);
                WebElement codeInput2 = driver.findElement(By.id("CN2"));
                codeInput2.sendKeys(codes[1]);
                WebElement codeInput3 = driver.findElement(By.id("CN3"));
                codeInput3.sendKeys(codes[2]);

                // Input the amount spent
                double amount = surveyData.getSpentAmount();


                String amountStr = Double.toString(amount);
                String[] amountParts = amountStr.split("\\.");
                WebElement amountSpentPounds = driver.findElement(By.id("AmountSpent1"));
                amountSpentPounds.sendKeys(amountParts[0]);
                WebElement amountSpentPence = driver.findElement(By.id("AmountSpent2"));
                amountSpentPence.sendKeys(amountParts[1]);

                // Click the "Start" button
                WebElement startButton = driver.findElement(By.id("NextButton"));
                startButton.click();
            }

            // Dined in at restaurant
            // Question - what was your visit type
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "What was your visit type?")) {
                WebElement dineInOption = driver.findElement(By.xpath("//*[@id='textR000005.1']"));
                dineInOption.click();
                WebElement dineInOptionNextButton = driver.findElement(By.id("NextButton"));
                dineInOptionNextButton.click();
            }


            // Find and fill out the receipt code input field
            WebElement receiptCodeInput = driver.findElement(By.id("Receipt"));
            receiptCodeInput.sendKeys(surveyData.getReceiptCode());

            // Find and fill out the spent amount input field
            WebElement spentAmountInput = driver.findElement(By.id("Amount"));
            spentAmountInput.sendKeys(String.valueOf(surveyData.getSpentAmount()));

            // Click the "Next" button
            WebElement nextButton = driver.findElement(By.id("NextButton"));
            nextButton.click();

            // Wait for the survey questions to load and answer them
            // Note: The following code assumes you know the structure of the survey and the IDs or other locators for the specific questions and buttons.


            // Continue to answer other survey questions
            // ...

            // Find and fill out the email input field
            WebElement emailInput = driver.findElement(By.id("email"));
            emailInput.sendKeys(surveyData.getEmail());

            // Click the "Submit" button
            WebElement submitButton = driver.findElement(By.id("SubmitButton"));
            submitButton.click();
        } finally {
            // Close the WebDriver
            driver.quit();
        }
    }
}
