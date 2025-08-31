//package com.elsewedyt.pdsapp.services;
//
//import java.io.*;
//import java.util.Properties;
//
//public class LoggingSetting {
//
//    private static final String CONFIG_DIR = "config";
//    private static final String CONFIG_FILE = CONFIG_DIR + File.separator + "config.properties";
//    private static final String USERNAME_KEY = "last_username";
//
//    // Save last used username to config/config.properties
//    public static void saveLastUsername(String username) {
//        try {
//            File configDir = new File(CONFIG_DIR);
//            if (!configDir.exists()) {
//                configDir.mkdirs(); // Create config folder if not exists
//            }
//
//            Properties props = new Properties();
//            File file = new File(CONFIG_FILE);
//
//            // Load existing properties if file exists
//            if (file.exists()) {
//                try (InputStream in = new FileInputStream(file)) {
//                    props.load(in);
//                }
//            }
//
//            props.setProperty(USERNAME_KEY, username);
//
//            try (OutputStream out = new FileOutputStream(file)) {
//                props.store(out, null);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace(); // Or use your Logging class
//        }
//    }
//
//    // Read last used username from config/config.properties
//    public static String getLastUsername() {
//        File file = new File(CONFIG_FILE);
//        if (!file.exists()) {
//            return "";
//        }
//
//        try (InputStream in = new FileInputStream(file)) {
//            Properties props = new Properties();
//            props.load(in);
//            return props.getProperty(USERNAME_KEY, "");
//        } catch (IOException e) {
//            e.printStackTrace(); // Or use your Logging class
//            return "";
//        }
//    }
//
//}

package com.elsewedyt.pdsapp.services;

import com.elsewedyt.pdsapp.controllers.MainApp;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.util.Duration;

import java.io.File;

public class LoggingSetting {

     //Retrieves the current system user's username (Domain Username).

    public static String getCurrentUsername() {
        try {
            // Get the username from the system property
            String username = System.getProperty("user.name");
            return username != null ? username : "";
        } catch (Exception e) {
            e.printStackTrace(); // Log the error (replace with your logging mechanism if needed)
            return "";
        }
    }

    private static File currentJarFile;
    private static long jarLastModifiedTime = -1;
    public static void startJarUpdateWatcher() {
        ScheduledService<Void> jarCheckService = new ScheduledService<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        if (currentJarFile != null && currentJarFile.exists()) {
                            long currentModified = currentJarFile.lastModified();
                            if (currentModified > jarLastModifiedTime) {
                                jarLastModifiedTime = currentModified;

                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Application Update");
                                    alert.setHeaderText("The application has been updated.");
                                    alert.setContentText("يجب اغلاق التطبيق واعادة تشغيلة لوجود تحديث");
                                    alert.showAndWait();
                                });
                            }
                        }
                        return null;
                    }
                };
            }
        };
        jarCheckService.setPeriod(Duration.seconds(10));
        jarCheckService.start();
    }

    public static void initJarWatcher() {
        try {
            String path = MainApp.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();

            currentJarFile = new File(path);
            if (currentJarFile.exists()) {
                jarLastModifiedTime = currentJarFile.lastModified();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
