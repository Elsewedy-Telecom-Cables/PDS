

package com.etc.pdsapp.services;

import com.etc.pdsapp.controller.MainApp;
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
