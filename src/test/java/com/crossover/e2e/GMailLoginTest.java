package com.crossover.e2e;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class GMailLoginTest {
    private static final String DRIVER_LOCATION = "D:\\EricDantas\\Crossover\\QA\\chromedriver_win32\\chromedriver.exe";
    private WebDriver driver;
    private Properties properties = new Properties();
    private GMailLogin objLogin;

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", DRIVER_LOCATION);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get("https://mail.google.com/");
        properties.load(new FileReader(new File("test.properties")));
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void testLoginToGMail() throws InterruptedException {

        // Create Login Page object
        objLogin = new GMailLogin(driver);

        // Verify login page title
        String loginPage = objLogin.getLoginPage();
        Assert.assertTrue(loginPage.toLowerCase().contains("login"));

        // Login to application
        objLogin.loginToGMail(properties.getProperty("username"), properties.getProperty("password"));

        waitForLoad(driver);

        // Check Inbox page
        String inboxPage = objLogin.getInbox();
        Assert.assertTrue(inboxPage.toLowerCase().contains("google apps"));
    }

    private void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = driver1 -> {
            assert driver1 != null;
            return ((JavascriptExecutor) driver1)
                    .executeScript("return document.readyState").equals("complete");
        };

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }
}