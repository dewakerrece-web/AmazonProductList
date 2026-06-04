package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AmazonSearchResultPage {

    WebDriver driver;

    By firstProduct = By.xpath("(//div[@data-component-type='s-search-result'])[1]");
    By productTitle = By.xpath(".//h2//span");
    By productPriceWhole = By.xpath(".//span[@class='a-price-whole']");
    By productPriceFraction = By.xpath(".//span[@class='a-price-fraction']");

    public AmazonSearchResultPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isSearchResultsLoaded() {
        return driver.findElements(firstProduct).size() > 0;
    }

    public WebElement getFirstProduct() {
        return driver.findElement(firstProduct);
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
            }
            return "Rupees " + whole + fraction;
        } catch (Exception e) {
            return "Price not available";
        }
    }
}
