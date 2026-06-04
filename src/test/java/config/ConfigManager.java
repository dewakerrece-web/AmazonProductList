package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigManager {

    private static ConfigManager instance;
    private final Properties properties;

    private ConfigManager() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(constants.FrameworkConstants.CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config file: " + constants.FrameworkConstants.CONFIG_FILE, e);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public String getUrl() {
        return properties.getProperty("url");
    }

    public String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }

    public int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicitWait", String.valueOf(constants.FrameworkConstants.EXPLICIT_WAIT_SECONDS)));
    }

    public int getPriceTolerance() {
        return Integer.parseInt(properties.getProperty("priceTolerance", String.valueOf(constants.FrameworkConstants.PRICE_TOLERANCE_PERCENT)));
    }
}
