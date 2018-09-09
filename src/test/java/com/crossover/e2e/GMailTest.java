package com.crossover.e2e;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GMailTest extends TestCase {
    private WebDriver driver;
    private Properties properties = new Properties();

    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "D:\\EricDantas\\Crossover\\QA\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();

        /*
        * BUG FIX 1:
        *     Wait until the page is fully loaded on every page navigation or page reload.
        * */
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

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

        /*
        * BUG FIX 2:
        *     Waiting for full load gmail inbox page
        **/
        waitForLoad(driver);

        /*
        * BUG FIX 3:
        *     Clicking in Compose (for non english cases)
        * */
        driver.findElement(By.cssSelector(".T-I.J-J5-Ji.T-I-KE.L3")).click();
        //WebElement composeElement = driver.findElement(By.xpath("//*[@role='button' and (.)='COMPOSE']"));
        //composeElement.click();

        Thread.sleep(1000);

        /* Type the username */
        driver.findElement(By.name("to")).clear();
        driver.findElement(By.name("to")).sendKeys(String.format("%s@gmail.com", properties.getProperty("username")));

        /* Type a subject */
        driver.findElement(By.name("subjectbox")).clear();
        driver.findElement(By.name("subjectbox")).sendKeys(String.format("This is a subject..."));

        Thread.sleep(1000);

        /* Type a body */
        driver.findElement(By.cssSelector(".Am.Al.editable.LW-avf")).click();
        driver.findElement(By.cssSelector(".Am.Al.editable.LW-avf")).clear();
        driver.findElement(By.cssSelector(".Am.Al.editable.LW-avf")).sendKeys(String.format("This is a body..."));

        Thread.sleep(1000);

        /* Attach a file */
        //driver.findElement(By.cssSelector(".a1.aaA.aMZ")).click();
        //WebElement chooseFile = driver.findElement((By.cssSelector(".a1.aaA.aMZ")));

        createSomeFile();

        Thread.sleep(1000);
        //chooseFile.sendKeys("C:\\selenium-tests\\test-file.txt");

        WebElement button2 = waitsss(driver, By.name("Filedata"));
        button2.sendKeys("C:\\selenium-tests\\test-file.txt");

        Thread.sleep(3000);

        /* Sending email... */
        driver.findElement(By.cssSelector(".T-I.J-J5-Ji.aoO.T-I-atl.L3")).click();
        //driver.findElement(By.xpath("//*[@role='button' and text()='Send']")).click();

        Thread.sleep(3000);

        /* Wait for email and check it */

        List<WebElement> emails = driver.findElements(By.xpath("//*[@class='yW']/span"));
        System.out.println(emails.size());
        for (WebElement we : emails) {
            System.out.println(we.getText());

            if (we.getText().equals("This is a subject...")) //to click on a specific mail.
            {
                we.click();
                break;
            }
        }
    }

    public void createSomeFile() throws IOException {
        String str = "Crossover online hiring event...";
        byte[] strToBytes = str.getBytes();

        Path path = Paths.get("C:\\selenium-tests");
        boolean pathExists = Files.exists(path);
        if (!pathExists) {
            try {
                Path newDir = Files.createDirectory(path);
            } catch (FileAlreadyExistsException e){
                // the directory already exists.
                path = Paths.get("C:\\selenium-tests\\test-file.txt");
                pathExists = Files.exists(path);
                if (!pathExists) {
                    try {
                        Path newFile = Files.createFile(path);
                        Files.write(path, strToBytes);

                    } catch (FileAlreadyExistsException f) {
                        Files.write(path, strToBytes);
                    }
                }

            } catch (IOException e) {
                //something else went wrong
                e.printStackTrace();
            }
        } else {
            path = Paths.get("C:\\selenium-tests\\test-file.txt");
            pathExists = Files.exists(path);
            if (!pathExists) {
                try {
                    Path newFile = Files.createFile(path);
                    Files.write(path, strToBytes);
                } catch (FileAlreadyExistsException f) {
                    Files.write(path, strToBytes);
                }
            }
        }
    }

    private WebElement waitsss(WebDriver driver, By elementIdentifier){
        Wait<WebDriver> wait =
                new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
                        .pollingEvery(1, TimeUnit.SECONDS)
                        .ignoring(NoSuchElementException.class);

        return wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(elementIdentifier);
            }
        });
    }

    private void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor)driver)
                        .executeScript("return document.readyState").equals("complete");
            }
        };

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }
}
