package com.obs.myTestSelenium;

import com.obs.myTestSelenium.ExcelUtility;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class MyTest {
    public WebDriver driver;

    public void testInitialize(String browser, String url) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else {
            try {
                throw new Exception("Browser value not defined");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        driver.manage().deleteAllCookies();
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser","url"})
    public void setUp(String browservalue,String urlvalue) {
        //testInitialize("chrome", "http://demowebshop.tricentis.com/");
        testInitialize(browservalue,urlvalue);
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        if (result.getStatus() ==ITestResult.FAILURE) {
            TakesScreenshot scr = (TakesScreenshot) driver;
            File screenshot = scr.getScreenshotAs(OutputType.FILE);
            File dest = new File("./ScreenShots/" + result.getName() + ".png");
            FileUtils.copyFile(screenshot, dest);
        }
        driver.quit();
    }

    @Test(priority = 1, description = "TC_001 verifyPageTitle",groups = {"smoke"})
    public void verifyPageTitle() {
        String act_Title = driver.getTitle();
        String exp_Title = "Demo Web Shop";
        Assert.assertEquals(act_Title, exp_Title, "Title not matching");
    }

    @Test(priority = 3, description = "TC002 verifyJavaScriptClickAndSendKeys",groups = {"smoke"})
    public void verifyJavaScriptClickAndSendKeys() {
        driver.findElement(By.xpath("//a[text()='Log in']")).click();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String s1 = "document.getElementById('Email').value=\"selenium121@test.com\"";
        String s2 = "document.getElementById('Password').value=\"12345678\"";
        js.executeScript(s1);
        js.executeScript(s2);
        driver.findElement(By.xpath("//input[@value='Log in']"));
    }

    @Test(dataProvider = "LoginWebShopFromExcel",priority = 2,description = "TC003 verifyLoginByDataProviderAndExcel",groups = {"regression"})
    public void verifyLoginByDataProviderAndExcel(String username,String password){
        driver.findElement(By.xpath("//a[text()='Log in']")).click();
        driver.findElement(By.xpath("//a[text()='Log in']")).click();
        WebElement user = driver.findElement(By.xpath("//input[@id='Email']"));
        user.sendKeys(username);
        WebElement pass = driver.findElement(By.xpath("//input[@id='Password']"));
        pass.sendKeys(password);
        driver.findElement(By.xpath("//input[@class='button-1 login-button']")).click();
    }
    @DataProvider(name="LoginWebShopFromExcel")
    public Object[][] loginData() throws IOException {
        String filePath=System.getProperty("user.dir")+"\\src\\main\\resources\\testdata.xlsx";
        ExcelUtility excelUtility=new ExcelUtility();
        Object[][] data=excelUtility.excelRead(filePath,"login");
        return data;
    }
}
