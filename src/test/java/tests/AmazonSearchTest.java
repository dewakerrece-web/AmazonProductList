package tests;

import base.BaseTest;
import constants.FrameworkConstants;
import pages.AmazonHomePage;
import pages.AmazonSearchResultPage;
import utils.ExcelUtility;
import utils.ReportUtility;
import utils.ScreenshotUtility;

import java.util.ArrayList;
import java.util.List;

public class AmazonSearchTest extends BaseTest {

    @org.testng.annotations.Test
    public void searchAllProducts() {
        AmazonHomePage homePage;
        AmazonSearchResultPage searchResultPage;
        List<String[]> resultsData = new ArrayList<>();

        ExcelUtility excel = new ExcelUtility(FrameworkConstants.INPUT_FILE);
        List<Object[]> brandData = excel.readBrandData(FrameworkConstants.EXCEL_SHEET_NAME);

        for (int i = 0; i < brandData.size(); i++) {
            String brandName = (String) brandData.get(i)[0];
            double expectedPrice = (double) brandData.get(i)[1];

            System.out.println("------------------------------------------");
            System.out.println("Searching for: " + brandName);
            System.out.println("Expected Price: Rupees " + (int) expectedPrice);

            String productName = "";
            String actualPrice = "";
            String status = "FAIL";
            String screenshotPath = "";

            try {
                homePage = new AmazonHomePage(driver);
                if (homePage.isSearchBoxDisplayed()) {
                    System.out.println("Amazon home page loaded successfully.");
                }

                homePage.searchProduct(brandName);
                System.out.println("Search performed for: " + brandName);

                searchResultPage = new AmazonSearchResultPage(driver);
                if (searchResultPage.isSearchResultsLoaded()) {
                    System.out.println("Search results loaded successfully.");
                }

                productName = searchResultPage.getFirstProductName();
                actualPrice = searchResultPage.getFirstProductPrice();

                System.out.println("Product found: " + productName);
                System.out.println("Price on Amazon: " + actualPrice);

                screenshotPath = ScreenshotUtility.captureScreenshot(driver, brandName);
                if (!screenshotPath.isEmpty()) {
                    System.out.println("Screenshot saved: " + screenshotPath);
                }

                String actualPriceDigits = actualPrice.replaceAll("[^0-9]", "");
                if (!actualPriceDigits.isEmpty() && expectedPrice > 0) {
                    double actualPriceValue = Double.parseDouble(actualPriceDigits);
                    double difference = Math.abs(actualPriceValue - expectedPrice);
                    double percentDiff = (difference / expectedPrice) * 100;

                    System.out.println("Expected: Rupees " + (int) expectedPrice);
                    System.out.println("Actual: Rupees " + (int) actualPriceValue);
                    System.out.println("Difference: " + String.format("%.1f", percentDiff) + "%");

                    status = (percentDiff <= FrameworkConstants.PRICE_TOLERANCE_PERCENT) ? "PASS" : "FAIL";
                } else {
                    status = "PASS";
                }

            } catch (Exception e) {
                System.out.println("Error for " + brandName + ": " + e.getMessage());
                ScreenshotUtility.captureScreenshot(driver, brandName + "_FAIL");
                status = "FAIL";
            }

            System.out.println("Status: " + status);
            resultsData.add(new String[]{
                    brandName, productName, String.valueOf((int) expectedPrice), actualPrice, status
            });
        }

        writeOutput(resultsData);
    }

    private void writeOutput(List<String[]> resultsData) {
        try {
            ExcelUtility excel = new ExcelUtility(FrameworkConstants.OUTPUT_FILE);
            excel.writeResult(FrameworkConstants.EXCEL_SHEET_NAME, resultsData);
            System.out.println("Results written to: " + FrameworkConstants.OUTPUT_FILE);
        } catch (Exception e) {
            System.out.println("Error writing Excel output: " + e.getMessage());
        }

        ReportUtility.generateExecutionReport(resultsData);
    }
}
