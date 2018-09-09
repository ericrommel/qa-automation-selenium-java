package com.crossover.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class GMailCheckEmail {
    private WebDriver driver;

    private By emails = By.xpath("//*[@class='y6']/span");
    private By receivedEmail = By.cssSelector(".xY.a4W");
    private By subject = By.className("hP");
    private By body = By.cssSelector(".a3s.aXjCH");
    private By attachedName = By.cssSelector(".aV3.zzV0ie");

    public GMailCheckEmail(WebDriver driver) {
        this.driver = driver;
    }

    public List<WebElement> getEmails() {
        return driver.findElements(emails);
    }

    public void openReceivedEmail() {
        driver.findElement(receivedEmail).click();
    }

    public String getSubject() {
        return driver.findElement(subject).getText();
    }

    public String getBody() {
        return driver.findElement(body).getText();
    }

    public String getAttachedName() {
        return driver.findElement(attachedName).getText();
    }
}
