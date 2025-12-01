package com.etc.pdsapp.services;

import com.etc.pdsapp.controller.AddUserController;
import com.etc.pdsapp.controller.LoginController;
import com.etc.pdsapp.db.DEF;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.Consumer;

import static com.etc.pdsapp.logging.Logging.ERROR;
import static com.etc.pdsapp.logging.Logging.logException;

public class WindowUtils {

    public static void OPEN_WINDOW(String fxmlPath, Runnable onCloseAction) {
        try {
            Parent root = FXMLLoader.load(WindowUtils.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(WindowUtils.class.getResourceAsStream(iconImagePath)));

            if (onCloseAction != null)
                // Handle window close event
                stage.setOnCloseRequest(event -> onCloseAction.run());

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void OPEN_WINDOW_FULL_SCREEN(String fxmlPath, Runnable onCloseAction) {
        try {
            Parent root = FXMLLoader.load(WindowUtils.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(WindowUtils.class.getResourceAsStream(iconImagePath)));

            if (onCloseAction != null) {
                stage.setOnCloseRequest(event -> onCloseAction.run());
            }

            stage.setResizable(true);      // Allow resizing
            stage.setMaximized(true);      // Open maximized by default

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void OPEN_WINDOW_FULL_SCREEN2(String fxmlPath, Runnable onCloseAction) {
        try {
            Parent root = FXMLLoader.load(WindowUtils.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(WindowUtils.class.getResourceAsStream(iconImagePath)));

            if (onCloseAction != null) {
                stage.setOnCloseRequest(event -> onCloseAction.run());
            }

            stage.setResizable(true);      // Allow resizing
         //   stage.setMaximized(true);      // Open maximized by default

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void OPEN_WINDOW_NOT_RESIZABLE(String fxmlPath, Runnable onCloseAction) {
        try {
            Parent root = FXMLLoader.load(WindowUtils.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(WindowUtils.class.getResourceAsStream(iconImagePath)));
            if (onCloseAction != null)
                // Handle window close event
                stage.setOnCloseRequest(event -> onCloseAction.run());

            stage.show();
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface StageAware {
        void setStage(Stage stage);
    }
    public static void OPEN_WINDOW_NOT_RESIZABLE_2(String fxmlPath, Runnable onCloseAction) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtils.class.getResource(fxmlPath));
            Parent root = loader.load(); // تحميل الصفحة مع الحفاظ على المرجع

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(WindowUtils.class.getResourceAsStream(iconImagePath)));

            //  إذا كان الكنترولر يحتاج معرفة الـ Stage
            Object controller = loader.getController();
            if (controller instanceof StageAware) {
                ((StageAware) controller).setStage(stage);
            }

            if (onCloseAction != null) {
                stage.setOnCloseRequest(event -> onCloseAction.run());
            }

            stage.show();
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void OPEN_WINDOW_NOT_RESIZABLE_3(String fxmlPath, Consumer<Object> controllerHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtils.class.getResource(fxmlPath));
            Parent root = loader.load();
            Object controller = loader.getController();

            if (controllerHandler != null) {
                controllerHandler.accept(controller);
            }

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void CLOSE(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public static void CLOSE2(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public static String iconImagePath = "/images/pdshome.png";
    public static int ALERT_WARNING = 1;
    public static int ALERT_ERROR = 2;
    public static int ALERT_CONFIRMATION = 3;
    public static int ALERT_INFORMATION = 4;

    // Calibration App  Screens
    public static final String LOGIN_PAGE = "/screens/Login.fxml";
    public static final String MAIN_PAGE = "/screens/Main.fxml";
    public static final String VIEW_USER_PAGE = "/screens/ViewUsers.fxml";
    public static final String ADD_USER_PAGE = "/screens/AddUser.fxml";
    public static final String  ADD_PREPARE_DATA = "/screens/AddPrepareData.fxml";
    public static final String  ADD_UPDATE_PDS_DATA = "/screens/AddUpdatePdsData.fxml";
    public static final String  EXPORT_PDS_DATA = "/screens/ExportPds.fxml";





    // Alert
    public static void ALERT(String header, String message, int type) {
        Alert.AlertType alertType = Alert.AlertType.INFORMATION;
        if (type == 1) {
            alertType = Alert.AlertType.WARNING;
        } else if (type == 2) {
            alertType = Alert.AlertType.ERROR;
        } else if (type == 3) {
            alertType = Alert.AlertType.CONFIRMATION;
        } else if (type == 4) {
            alertType = Alert.AlertType.INFORMATION;
        }
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void OPEN_LOGIN_PAGE() {
        try {
            OPEN_WINDOW_NOT_RESIZABLE(
                    LOGIN_PAGE,
                    null
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_LOGIN_PAGE", ex);
        }
    }
    public static void OPEN_MAIN_PAGE() {
        try {
            OPEN_WINDOW_NOT_RESIZABLE(
                    MAIN_PAGE,
                    () -> OPEN_LOGIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_MAIN_PAGE", ex);
        }
    }
    public static void OPEN_VIEW_USERS_PAGE() {
        try {
            OPEN_WINDOW_FULL_SCREEN(
                    VIEW_USER_PAGE,
                    () -> OPEN_MAIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_VIEW_USER_PAGE", ex);
        }
    }

    public static void OPEN_ADD_USER_PAGE() {
        try {
            OPEN_WINDOW_NOT_RESIZABLE(
                    ADD_USER_PAGE,
                    () -> OPEN_VIEW_USERS_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_ADD_USER_PAGE", ex);
        }
    }

    public static void OPEN_EDIT_USER_PAGE(int emp_code) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/screens/AddUser.fxml"));
            Parent parent = fxmlLoader.load(); // Load the FXML and get the root
            AddUserController addupdateuser_controller = fxmlLoader.getController();
            addupdateuser_controller.setUserData(emp_code, true);
            // Change Save Button from "حفظ" to " تعديل"
            addupdateuser_controller.setSaveButton();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Update User");
            stage.setOnCloseRequest(event -> OPEN_VIEW_USERS_PAGE());
            stage.show();
            stage.setResizable(false);
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_EDIT_USER_PAGE", ex);
        }
    }

    public static void OPEN_PREPARE_DATA_PAGE() {
        try {
            OPEN_WINDOW_FULL_SCREEN(
                    ADD_PREPARE_DATA,
                    () -> OPEN_MAIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_PREPARE_DATA_PAGE", ex);
        }
    }
    public static void OPEN_ADD_UPDATE_PDS_DATA() {
        try {
            OPEN_WINDOW_FULL_SCREEN2(
                    ADD_UPDATE_PDS_DATA,
                    () -> OPEN_MAIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_ADD_UPDATE_PDS_DATA", ex);
        }
    }
    public static void OPEN_EXPORT_PDS_DATA() {
        try {
            OPEN_WINDOW_FULL_SCREEN(
                    EXPORT_PDS_DATA,
                    () -> OPEN_MAIN_PAGE()
            );
        } catch (Exception ex) {
            logException(ERROR, WindowUtils.class.getName(), "OPEN_EXPORT_PDS_DATA", ex);
        }
    }





    public static int getUserRoleInt(String role) {
        if (DEF.USER_ROLE_GENERAL_USER_STRING.equals(role)) {
            return 0;
        } else if (DEF.USER_ROLE_USER_STRING.equals(role)) {
            return 1;
        } else if (DEF.USER_ROLE_SUPERVISOR_STRING.equals(role)) {
            return 2;
        } else if (DEF.USER_ROLE_ADMIN_STRING.equals(role)) {
            return 3;
        }
        return -1;
    }

    public static String getUserRoleStr(int roleId) {
        if (roleId == 0) {
            return DEF.USER_ROLE_GENERAL_USER_STRING;
        } else if (roleId == 1) {
            return DEF.USER_ROLE_USER_STRING;
        } else if (roleId == 2) {
            return DEF.USER_ROLE_SUPERVISOR_STRING;
        } else if (roleId == 3) {
            return DEF.USER_ROLE_ADMIN_STRING;
        }
        return null;
    }

    // UserActive Convert value Int to String and Reverse
    public static int getUserActiveInt(String active) {
        if (DEF.USER_ACTIVE_STRING.equals(active)) {
            return 1;
        } else if (DEF.USER_NOT_ACTIVE_STRING.equals(active)) {
            return 0;
        }
        return -1;
    }

    public static String getUserActiveStr(int active) {
        if (active == 1) {
            return DEF.USER_ACTIVE_STRING;
        } else if (active == 0) {
            return DEF.USER_NOT_ACTIVE_STRING;
        }
        return null;
    }



}

