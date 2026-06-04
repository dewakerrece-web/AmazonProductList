package listeners;

import driver.DriverManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtility;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String screenshotPath = ScreenshotUtility.captureScreenshot(
                DriverManager.getDriver(), methodName + "_FAIL");
        System.out.println("Screenshot captured for failed test: " + screenshotPath);
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("========== AMAZON PRODUCT SEARCH STARTED ==========");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("========== AMAZON PRODUCT SEARCH COMPLETED ==========");
    }
}
