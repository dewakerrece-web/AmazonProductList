package pages;

import config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AmazonSearchResultPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstProduct = By.xpath("(//div[@data-component-type='s-search-result'])[1]");
    private final By productTitle = By.xpath(".//h2//span");
    private final By productPriceWhole = By.xpath(".//span[@class='a-price-whole']");
    private final By productPriceFraction = By.xpath(".//span[@class='a-price-fraction']");

    public AmazonSearchResultPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getInstance().getExplicitWait()));
    }

    public boolean isSearchResultsLoaded() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(firstProduct));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement getFirstProduct() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(firstProduct));
    }

    public String getFirstProductName() {
        try {
            return getFirstProduct().findElement(productTitle).getText();
        } catch (Exception e) {
            return "Product name not found";
        }
    }

    public String getFirstProductPrice() {
        try {
            WebElement product = getFirstProduct();
            String whole = product.findElement(productPriceWhole).getText();
            String fraction = "";
            try {
                fraction = product.findElement(productPriceFraction).getText();
            } catch (Exception e) {
                // fraction may not exist
            }
            return "Rupees " + whole + fraction;
        } catch (Exception e) {
            return "Price not available";
        }
    }
}
