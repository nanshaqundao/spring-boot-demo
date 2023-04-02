package com.example.mcdemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class SurveySubmissionService {


    public boolean submitSurvey(SurveyData surveyData) {
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


            // Question - Please rate your overall satisfaction
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Please rate your overall satisfaction")) {
                WebElement radioButton = driver.findElement(By.xpath("//*[@id='FNSR000002']/td[2]/label"));
                radioButton.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Question - Where did you place your order?
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Where did you place your order?")) {
                WebElement radioButton = driver.findElement(By.xpath("//*[@id='textR000006.1']"));
                radioButton.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Question - How did you receive your order?
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "How did you receive your order?")) {
                WebElement radioButton = driver.findElement(By.xpath("//*[@id='textR000053.1']"));
                radioButton.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Question - Did you use My McDonald's Rewards on this visit?
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Did you use My McDonald's Rewards on this visit?")) {
                WebElement radioButton = driver.findElement(By.xpath("//*[@id='FNSR000384']/td[2]/label"));
                radioButton.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Matrix Questions - the speed of service
            // //*[@id="FNSR000017"]/td[2]/label
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "the speed of service")) {
                WebElement radioButton1 = driver.findElement(By.xpath("//*[@id='FNSR000017']/td[2]/label"));
                radioButton1.click();
                WebElement radioButton2 = driver.findElement(By.xpath("//*[@id='FNSR000010']/td[2]/label"));
                radioButton2.click();
                WebElement radioButton3 = driver.findElement(By.xpath("//*[@id='FNSR000012']/td[2]/label"));
                radioButton3.click();
                WebElement radioButton4 = driver.findElement(By.xpath("//*[@id='FNSR000009']/td[2]/label"));
                radioButton4.click();
                WebElement radioButton5 = driver.findElement(By.xpath("//*[@id='FNSR000018']/td[2]/label"));
                radioButton5.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Matrix Questions - the speed of service
            // //*[@id="FNSR000017"]/td[2]/label
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "The friendliness of the staff.")) {
                WebElement radioButton1 = driver.findElement(By.xpath("//*[@id='FNSR000019']/td[2]/label"));
                radioButton1.click();
                WebElement radioButton2 = driver.findElement(By.xpath("//*[@id='FNSR000020']/td[2]/label"));
                radioButton2.click();
                WebElement radioButton3 = driver.findElement(By.xpath("//*[@id='FNSR000011']/td[2]/label"));
                radioButton3.click();
                WebElement radioButton4 = driver.findElement(By.xpath("//*[@id='FNSR000016']/td[2]/label"));
                radioButton4.click();
                WebElement radioButton5 = driver.findElement(By.xpath("//*[@id='FNSR000008']/td[2]/label"));
                radioButton5.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }


            // Question - The overall value for the price you paid.
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "The overall value for the price you paid.")) {
                WebElement radioButton = driver.findElement(By.xpath("//*[@id='FNSR000021']/td[2]/label"));
                radioButton.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Question - Was your order accurate?
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Was your order accurate?")) {
                WebElement radioButton = driver.findElement(By.xpath("//*[@id='FNSR000052']/td[1]/label"));
                radioButton.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Question - Did you experience a problem on this occasion?
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Did you experience a problem on this occasion?")) {
                WebElement radioButton = driver.findElement(By.xpath("//*[@id='FNSR000026']/td[2]/label"));
                radioButton.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Question - Earlier you told us you were Satisfied with your experience. Please tell us what we could have done to make you Highly Satisfied at this
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Earlier you told us you were Satisfied with your experience. Please tell us what we could have done to make you Highly Satisfied at this")) {
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Question - Please select the items you ordered on this occasion. (Select all that apply.)
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Please select the items you ordered on this occasion. (Select all that apply.)")) {
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Question -  has affordable menu options.
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "has affordable menu options.")) {
                WebElement radioButton = driver.findElement(By.xpath("//*[@id='FNSR000499']/td[2]/label"));
                radioButton.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Question - Would you like to answer a few more questions about your experience?
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Would you like to answer a few more questions about your experience?")) {
                WebElement radioButton = driver.findElement(By.xpath("//*[@id='FNSR000466']/td[2]/label"));
                radioButton.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }

            // Question - How would you like to receive your validation code to redeem your offer?
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "How would you like to receive your validation code to redeem your offer?")) {
                WebElement radioButton = driver.findElement(By.xpath("//*[@id='textR000383.1']"));
                radioButton.click();
                WebElement overallSatisfactionNextButton = driver.findElement(By.id("NextButton"));
                overallSatisfactionNextButton.click();
            }


            // final page
            // Question - Please fill out your contact information below. This information will only be used to email you your incentive offer.
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "Please fill out your contact information below. This information will only be used to email you your incentive offer.")) {
                WebElement firstNameInput = driver.findElement(By.id("S000068"));
                WebElement secondNameInput = driver.findElement(By.id("S000073"));
                WebElement emailInput = driver.findElement(By.id("S000070"));
                WebElement confirmEmailInput = driver.findElement(By.id("S000071"));


                firstNameInput.sendKeys("Luke");
                secondNameInput.sendKeys("Skywalker");
                String emailString = surveyData.getEmail();
                emailInput.sendKeys(emailString);
                confirmEmailInput.sendKeys(emailString);

                WebElement nextButton = driver.findElement(By.id("NextButton"));
                nextButton.click();
            }

            // final - You will receive an email shortly with your voucher.
            if (PageQuestionUtil.isPageHavingQuestionText(driver, "You will receive an email shortly with your voucher.")) {
                return true;
            }

            return false;
        } finally {
            // Close the WebDriver
            driver.quit();
        }
    }
}
