package pages;

import config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AmazonHomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By searchBox = By.id("twotabsearchtextbox");

    public AmazonHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getInstance().getExplicitWait()));
    }

    public boolean isSearchBoxDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void searchProduct(String brandName) {
        wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(brandName);
        driver.findElement(searchBox).sendKeys(Keys.ENTER);
    }
}
