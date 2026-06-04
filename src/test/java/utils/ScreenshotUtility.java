package utils;

import constants.FrameworkConstants;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ScreenshotUtility {

    private ScreenshotUtility() {}

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            Path screenshotDir = Paths.get(FrameworkConstants.SCREENSHOT_FOLDER);
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String filePath = screenshotDir.resolve(screenshotName + ".png").toString();
            Files.copy(source.toPath(), Paths.get(filePath));
            return filePath;
        } catch (IOException e) {
            System.err.println("Screenshot error: " + e.getMessage());
            return "";
        }
    }
}
