package com.etc.pdsapp.controller;
import com.etc.pdsapp.db.DbConnect;
import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.services.LoggingSetting;
import com.etc.pdsapp.services.WindowUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        try {

            Connection connection = DbConnect.getConnect();
            if (connection == null) {
             WindowUtils.ALERT("ERROR  ", "Cannot connect to database", WindowUtils.ALERT_ERROR);
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/screens/Login.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Login Form");
                stage.setScene(scene);
                stage.show();
                stage.setResizable(false);
               LoggingSetting.startJarUpdateWatcher();
               LoggingSetting.initJarWatcher();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logException("ERROR", this.getClass().getName(), "start", e);
            Logging.logException("ERROR", this.getClass().getName(), "start", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}



