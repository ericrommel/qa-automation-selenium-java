package com.crossover.e2e.poms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GMailLogin {
    private WebDriver driver;

    private By loginPage = By.id("headingText");
    private By identifierId = By.id("identifierId");
    private By password = By.name("password");
    private By btnIdentifierNext = By.id("identifierNext");
    private By btnPasswordNext = By.id("passwordNext");
    private By inbox = By.cssSelector(".gb_b"); // Search the Google Apps box for ensure that is in the inbox page

    public GMailLogin (WebDriver driver) {
        this.driver = driver;
    }

    private void setIdentifierId(String username) {
        driver.findElement(identifierId).sendKeys(username);
    }

    private void setPassword(String pass) {
        driver.findElement(password).sendKeys(pass);
    }

    private void clickIdentifierNext() {
        driver.findElement(btnIdentifierNext).click();
    }

    private void clickPasswordNext() {
        driver.findElement(btnPasswordNext).click();
    }

    public String getLoginPage() {
        return driver.findElement(loginPage).getText();
    }

    public String getInbox(){
        String googleApps = "aria-label";
        return driver.findElement(inbox).getAttribute(googleApps);
    }

    /**
     * This POM method will be exposed in test case to login in GMail
     * @param username Gmail's username
     * @param pass Gmail's password
     */
    public void loginToGMail(String username,String pass) throws InterruptedException {
        this.setIdentifierId(username);
        this.clickIdentifierNext();
        Thread.sleep(2000);
        this.setPassword(pass);
        this.clickPasswordNext();
    }
}
