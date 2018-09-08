package com.crossover.e2e;

import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Properties;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class GMailTest extends TestCase {
    private WebDriver driver;
    private Properties properties = new Properties();

    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Manual Installs\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();
        properties.load(new FileReader(new File("test.properties")));
    }

    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void testSendEmail() throws Exception {
        driver.get("https://mail.google.com/");
        WebElement userElement = driver.findElement(By.id("identifierId"));
        userElement.sendKeys(properties.getProperty("username"));

        driver.findElement(By.id("identifierNext")).click();

        Thread.sleep(1000);

        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys(properties.getProperty("password"));
        driver.findElement(By.id("passwordNext")).click();

        Thread.sleep(1000);

        WebElement composeElement = driver.findElement(By.xpath("//*[@role='button' and (.)='COMPOSE']"));
        composeElement.click();

        driver.findElement(By.name("to")).clear();
        driver.findElement(By.name("to")).sendKeys(String.format("%s@gmail.com", properties.getProperty("username")));
        driver.findElement(By.xpath("//*[@role='button' and text()='Send']")).click();
    }
}
