package com.crossover.e2e;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/*
*
* NOTE: https://seleniumhq.github.io/docs/worst.html: GMAIL, EMAIL, AND FACEBOOK LOGINS
* */
public class GMailTest {
    private static final String A_SUBJECT = "This is a subject...";
    private static final String A_BODY = "This is a body...";
    private static final String TEST_FILE = "/test-file.txt";
    private static final String DRIVER_LOCATION = "D:\\EricDantas\\Crossover\\QA\\chromedriver_win32\\chromedriver.exe";
    private WebDriver driver;
    private Properties properties = new Properties();

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", DRIVER_LOCATION);
        driver = new ChromeDriver();

        /*
        * BUG FIX 1:
        *     Wait until the page is fully loaded on every page navigation or page reload.
        * */
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        driver.get("https://mail.google.com/");

        properties.load(new FileReader(new File("test.properties")));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testSendEmail() throws Exception {

        /* TASK 1: Log to Gmail */
        logInGmail();

        /* TASK 2: Compose an email with unique subject, body, and attachment */
        /*
        * BUG FIX 3:
        *     Clicking on Compose (for non english cases)
        * */
        driver.findElement(By.cssSelector(".T-I.J-J5-Ji.T-I-KE.L3")).click();
        //WebElement composeElement = driver.findElement(By.xpath("//*[@role='button' and (.)='COMPOSE']"));
        //composeElement.click();
        WebElement composeWindow = driver.findElement(By.id(":mz"));
        Assert.assertTrue(composeWindow.isDisplayed());

        Thread.sleep(1000);

        /* Type the username */
        driver.findElement(By.name("to")).clear();
        driver.findElement(By.name("to")).sendKeys(String.format("%s@gmail.com", properties.getProperty("username")));

        /* Type a subject */
        driver.findElement(By.name("subjectbox")).clear();
        driver.findElement(By.name("subjectbox")).sendKeys(A_SUBJECT);

        Thread.sleep(1000);

        /* Type a body */
        driver.findElement(By.cssSelector(".Am.Al.editable.LW-avf")).click();
        driver.findElement(By.cssSelector(".Am.Al.editable.LW-avf")).clear();
        driver.findElement(By.cssSelector(".Am.Al.editable.LW-avf")).sendKeys(A_BODY);

        Thread.sleep(1000);

        /* Attach a file */
        //driver.findElement(By.cssSelector(".a1.aaA.aMZ")).click();
        //WebElement chooseFile = driver.findElement((By.cssSelector(".a1.aaA.aMZ")));

        Thread.sleep(1000);

        File fileToBeAttached = new File(this.getClass().getResource(TEST_FILE).getFile());

        WebElement webElement = waitElement(driver, By.name("Filedata"));
        webElement.sendKeys(fileToBeAttached.getAbsolutePath());

        Thread.sleep(3000);

        /* TASK 3: Send the email to the same account which was used to login (from and to addresses would be the same) */
        /*
         * BUG FIX 4:
         *     Clicking on Send (for non english cases)
         * */
        driver.findElement(By.cssSelector(".T-I.J-J5-Ji.aoO.T-I-atl.L3")).click();
        //driver.findElement(By.xpath("//*[@role='button' and text()='Send']")).click();

        Thread.sleep(3000);

        /* TASK 4: Wait for the email to arrive in the Inbox */
        /* Searching email and click on it */
        List<WebElement> emails = driver.findElements(By.xpath("//*[@class='y6']/span"));
        for (WebElement we : emails) {
            if (we.getText().equals(A_SUBJECT)) {
                /* TASK 5: Open the received email */
                driver.findElement(By.cssSelector(".xY.a4W")).click();
                waitForLoad(driver);

                /* TASK 6: Verify the subject, body and attachment name of the received email */
                /* Assert to SUBJECT */
                String subject = driver.findElement(By.className("hP")).getText();
                Assert.assertEquals(A_SUBJECT, subject);

                /* Assert to BODY */
                String body = driver.findElement(By.cssSelector(".a3s.aXjCH")).getText();
                Assert.assertEquals(A_BODY, body);

                /* Assert to ATTACHMENT */
                String attachedName = driver.findElement(By.cssSelector(".aV3.zzV0ie")).getText();
                Assert.assertEquals(fileToBeAttached.getName(), attachedName);

                Thread.sleep(1000);

                break;
            }
        }
    }

    private void logInGmail() throws InterruptedException {
        WebElement loginPage = driver.findElement(By.id("headingText"));
        Assert.assertEquals("Login", loginPage.getText());

        WebElement userElement = driver.findElement(By.id("identifierId"));
        userElement.sendKeys(properties.getProperty("username"));

        driver.findElement(By.id("identifierNext")).click();

        Thread.sleep(2000);

        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys(properties.getProperty("password"));
        driver.findElement(By.id("passwordNext")).click();

        /*
         * BUG FIX 2:
         *     Waiting for full load gmail inbox page
         **/
        waitForLoad(driver);

        String inbox = driver.findElement(By.cssSelector(".gb_b")).getAttribute("aria-label");
        Assert.assertEquals("Google Apps", inbox);
    }

    private WebElement waitElement(WebDriver driver, By elementIdentifier){
        Wait<WebDriver> wait =
                new FluentWait<>(driver).withTimeout(60, TimeUnit.SECONDS)
                        .pollingEvery(1, TimeUnit.SECONDS)
                        .ignoring(NoSuchElementException.class);

        return wait.until(driver1 -> {
            assert driver1 != null;
            return driver1.findElement(elementIdentifier);
        });
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
