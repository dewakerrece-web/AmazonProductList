package base;

import config.ConfigManager;
import driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.createDriver();
        driver.get(ConfigManager.getInstance().getUrl());
        System.out.println("Browser opened. Navigated to: " + ConfigManager.getInstance().getUrl());
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        System.out.println("Browser closed.");
    }
}
