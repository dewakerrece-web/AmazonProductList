package tests;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import base.BaseTest;
import pages.AmazonHomePage;
import pages.AmazonSearchResultPage;
import utils.ExcelUtility;

public class AmazonSearchTest extends BaseTest {

    AmazonHomePage homePage;
    AmazonSearchResultPage searchResultPage;
    List<String[]> resultsData = new ArrayList<>();

    String inputFile = "input/InputData.xlsx";
    String outputFile = "output/OutputData.xlsx";
    String screenshotFolder = "screenshots";
    String reportFolder = "reports";

    @BeforeClass
    public void startMessage() {
        System.out.println("========== AMAZON PRODUCT SEARCH STARTED ==========");
    }

    @Test
    public void searchAllProducts() {

        setup();

        ExcelUtility excel = new ExcelUtility(inputFile);
        List<Object[]> brandData = excel.readBrandData("Sheet1");

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

                screenshotPath = takeScreenshot(brandName);
                if (!screenshotPath.equals("")) {
                    System.out.println("Screenshot saved: " + screenshotPath);
                }

                String actualPriceDigits = actualPrice.replaceAll("[^0-9]", "");
                if (!actualPriceDigits.equals("") && expectedPrice > 0) {
                    double actualPriceValue = Double.parseDouble(actualPriceDigits);
                    double difference = Math.abs(actualPriceValue - expectedPrice);
                    double percentDiff = (difference / expectedPrice) * 100;

                    System.out.println("Expected: Rupees " + (int) expectedPrice);
                    System.out.println("Actual: Rupees " + (int) actualPriceValue);
                    System.out.println("Difference: " + String.format("%.1f", percentDiff) + "%");

                    if (percentDiff <= 20) {
                        status = "PASS";
                    } else {
                        status = "FAIL";
                    }
                } else {
                    status = "PASS";
                }

            } catch (Exception e) {
                System.out.println("Error for " + brandName + ": " + e.getMessage());
                screenshotPath = takeScreenshot(brandName + "_FAIL");
                status = "FAIL";
            }

            System.out.println("Status: " + status);
            resultsData.add(new String[] { brandName, productName, String.valueOf((int) expectedPrice), actualPrice, status });
        }

        tearDown();
    }

    public String takeScreenshot(String brandName) {
        try {
            File screenshotFolderObj = new File(screenshotFolder);
            if (!screenshotFolderObj.exists()) {
                screenshotFolderObj.mkdirs();
            }
            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String filePath = screenshotFolder + "/" + brandName + ".png";
            Files.copy(sourceFile, new File(filePath));
            return filePath;
        } catch (Exception e) {
            System.out.println("Screenshot error: " + e.getMessage());
            return "";
        }
    }

    @AfterClass
    public void finishExecution() {

        try {
            ExcelUtility excel = new ExcelUtility(outputFile);
            excel.writeResult("Sheet1", resultsData);
            System.out.println("Results written to: " + outputFile);
        } catch (Exception e) {
            System.out.println("Error writing Excel output: " + e.getMessage());
        }

        try {
            File reportFolderObj = new File(reportFolder);
            if (!reportFolderObj.exists()) {
                reportFolderObj.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = reportFolder + "/ExecutionReport_" + timestamp + ".txt";
            FileWriter writer = new FileWriter(reportPath);

            writer.write("AMAZON AUTOMATION EXECUTION REPORT\n");
            writer.write("Generated: " + new Date().toString() + "\n\n");

            for (String[] row : resultsData) {
                writer.write("========================================\n");
                writer.write("Brand Name      : " + row[0] + "\n");
                writer.write("Product Name    : " + row[1] + "\n");
                writer.write("Expected Price  : " + row[2] + "\n");
                writer.write("Actual Price    : " + row[3] + "\n");
                writer.write("Status          : " + row[4] + "\n");
                writer.write("========================================\n\n");
            }

            writer.close();
            System.out.println("Report saved: " + reportPath);
        } catch (Exception e) {
            System.out.println("Error writing report: " + e.getMessage());
        }

        System.out.println("========== AMAZON PRODUCT SEARCH COMPLETED ==========");
    }
}
