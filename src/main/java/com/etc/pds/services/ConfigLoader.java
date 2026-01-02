package com.etc.pds.services;

import com.etc.pds.logging.Logging;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }
            properties.load(input);
        } catch (Exception ex) {
            Logging.logException("ERROR", ConfigLoader.class.getName(), "load configuration", ex);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
