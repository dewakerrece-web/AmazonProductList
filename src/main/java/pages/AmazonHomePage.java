package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys;

public class AmazonHomePage {

    WebDriver driver;

    By searchBox = By.id("twotabsearchtextbox");
    By searchButton = By.id("nav-search-submit-button");

    public AmazonHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void searchProduct(String brandName) {
        driver.findElement(searchBox).clear(); //locators
        driver.findElement(searchBox).sendKeys(brandName);
        driver.findElement(searchBox).sendKeys(Keys.ENTER);
    }

    public boolean isSearchBoxDisplayed() {
        return driver.findElement(searchBox).isDisplayed();
    }
}
