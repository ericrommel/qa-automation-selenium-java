package com.crossover.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;

public class GMailSendEmail {
    private WebDriver driver;

    private By composeWindow = By.id(":mz");
    private By toField = By.name("to");
    private By subjectBox = By.name("subjectbox");
    private By body = By.cssSelector(".Am.Al.editable.LW-avf");
    private By fileData = By.name("Filedata");
    private By btnCompose = By.cssSelector(".T-I.J-J5-Ji.T-I-KE.L3");
    private By btnSend = By.cssSelector(".T-I.J-J5-Ji.aoO.T-I-atl.L3");

    public GMailSendEmail(WebDriver driver) {
        this.driver = driver;
    }

    public Boolean isComposeWindowDisplayed() {
        return driver.findElement(composeWindow).isDisplayed();
    }

    private void setToField(String to) {
        driver.findElement(toField).sendKeys(to);
    }

    private void setSubjectBox(String subject) {
        driver.findElement(subjectBox).sendKeys(subject);
    }

    private void setBody(String bodyText) {
        driver.findElement(body).sendKeys(bodyText);
    }

    private void setFileData(File fileName) {
        driver.findElement(fileData).sendKeys(fileName.getAbsolutePath());
    }

    private void clickBtnCompose() {
        driver.findElement(btnCompose).click();
    }

    private void clickBtnSend() {
        driver.findElement(btnSend).click();
    }

    /**
     * This POM method will be exposed in test case to send an email in GMail
     * @param toUser username to be sent
     * @param subject subject of email
     * @param bodyText body of email
     * @param filedata file to be attached
     */
    public void sendEmail(String toUser, String subject, String bodyText, File filedata) throws InterruptedException {
        this.setToField(toUser);
        this.setSubjectBox(subject);
        Thread.sleep(1000);
        this.setBody(bodyText);
        Thread.sleep(1000);
        this.setFileData(filedata);
        Thread.sleep(2000);
        this.clickBtnSend();
    }

    /**
     * This POM method will open the compose window
     */
    public void openComposeWindow() {
        this.clickBtnCompose();
    }
}
