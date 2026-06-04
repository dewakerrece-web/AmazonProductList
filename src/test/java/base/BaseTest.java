package base;

import java.io.FileInputStream;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

    public WebDriver driver;

    public void setup() {
        try {
            Properties config = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            config.load(fis);
            fis.close();

            String url = config.getProperty("url");
            String browser = config.getProperty("browser");

            if (browser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                driver = new ChromeDriver(options);
            }

            driver.get(url);
            System.out.println("Browser opened. Navigated to: " + url);

        } catch (Exception e) {
            System.out.println("Error in setup: " + e.getMessage());
        }
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed.");
        }
    }
}
