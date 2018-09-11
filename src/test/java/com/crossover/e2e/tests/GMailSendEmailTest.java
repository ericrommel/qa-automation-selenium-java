package com.crossover.e2e.tests;

import com.crossover.e2e.poms.GMailCheckEmail;
import com.crossover.e2e.poms.GMailLogin;
import com.crossover.e2e.poms.GMailSendEmail;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class GMailSendEmailTest {
    private static final String DRIVER_LOCATION = "D:\\EricDantas\\Crossover\\QA\\chromedriver_win32\\chromedriver.exe";
    private WebDriver driver;
    private Properties properties = new Properties();
    private static final String A_SUBJECT = "This is a subject...";
    private static final String A_BODY = "This is a body...";
    private static final String TEST_FILE = "/test-file.txt";

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", DRIVER_LOCATION);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get("https://mail.google.com/");
        properties.load(new FileReader(new File("test.properties")));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testSendEmail() throws InterruptedException {
        // Create Login Page object
        GMailLogin objLogin = new GMailLogin(driver);

        // Verify login page title
        String loginPage = objLogin.getLoginPage();
        Assert.assertTrue(loginPage.toLowerCase().contains("login"));

        // Login to application
        objLogin.loginToGMail(properties.getProperty("username"), properties.getProperty("password"));

        waitForLoad(driver);

        // Check Inbox page
        String inboxPage = objLogin.getInbox();
        Assert.assertTrue(inboxPage.toLowerCase().contains("google apps"));

        // Create SendEmail Page object
        GMailSendEmail objSendEmail = new GMailSendEmail(driver);

        // Open compose window
        objSendEmail.openComposeWindow();
        Boolean isOpened = objSendEmail.isComposeWindowDisplayed();
        waitForLoad(driver);
        Assert.assertTrue(isOpened);

        // Send an email
        File fileToBeAttached = new File(this.getClass().getResource(TEST_FILE).getFile());
        String user = String.format("%s@gmail.com", properties.getProperty("username"));
        objSendEmail.sendEmail(user, A_SUBJECT, A_BODY, fileToBeAttached);
        waitForLoad(driver);

        // Create Check email Page object
        GMailCheckEmail objCheckEmail = new GMailCheckEmail(driver);

        // Check email received
        List<WebElement> emails = objCheckEmail.getEmails();
        for (WebElement we : emails) {
            if (we.getText().equals(A_SUBJECT)) {
                objCheckEmail.openReceivedEmail();

                /* Assert to SUBJECT */
                String subject = objCheckEmail.getSubject();
                Assert.assertEquals(A_SUBJECT, subject);

                /* Assert to BODY */
                String body = objCheckEmail.getBody();
                Assert.assertEquals(A_BODY, body);

                /* Assert to ATTACHMENT */
                String attachedName = objCheckEmail.getAttachedName();
                Assert.assertEquals(fileToBeAttached.getName(), attachedName);

                Thread.sleep(1000);

                break;
            }
        }

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