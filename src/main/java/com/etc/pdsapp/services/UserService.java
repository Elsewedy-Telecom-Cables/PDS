package com.etc.pdsapp.services;

import com.etc.pdsapp.dao.UserDao;
import com.etc.pdsapp.model.User;
import com.etc.pdsapp.model.UserContext;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

import static com.etc.pdsapp.services.SettingService.APP_BUNDLE;
import static com.etc.pdsapp.services.WindowUtils.ALERT;
import static com.etc.pdsapp.services.WindowUtils.ALERT_WARNING;

public class UserService {

    private static UserDao userDao;

    public UserService() {

        this.userDao = new UserDao();
    }

    public Optional<User> loadUserData(int userId) {
        try {
            return Optional.ofNullable(userDao.loadUserData(userId));
        } catch (Exception ex) {
            //    LOG_EXCEP(this.getClass().getName(), "loadUserData", ex);
            return Optional.empty();
        }
    }

    public int checkLogIn(String userName, String pass) {
        // Check for empty or null username
        if (userName == null || userName.isEmpty()) {
            ALERT("", APP_BUNDLE().getString("USER_NAME_INVALID"), ALERT_WARNING);
        }
        // Check for empty or null password
        if (pass == null || pass.isEmpty()) {
            ALERT("", APP_BUNDLE().getString("PASSWORD_INVALID"), ALERT_WARNING);
        }
        // If both username and password are valid, attempt to log in
        if ((userName != null && !userName.isEmpty()) && (pass != null && !pass.isEmpty())) {
          //  return userDao.checkLogin(userName, pass);
            return -1;
        } else {
            return -1;
        }
    }

// in Class UserService
    public static boolean confirmPassword(String currentUsername) {
        userDao = new UserDao();
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Confirm Password");
        dialog.setHeaderText("Please enter password to confirm");

        ButtonType confirmButtonType = new ButtonType("ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Password:"), 0, 0);
        grid.add(passwordField, 1, 0);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        // Focus the password field by default
        Platform.runLater(passwordField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        return result.map(pass -> {
            User user = userDao.checkConfirmPassword(currentUsername, pass);
            int userId = -1;
            if(user!= null) {
                 userId = user.getUserId();
            }
            return userId != -1;
        }).orElse(false);
    }

    public static boolean usersAuthorization() {
        String username = UserContext.getCurrentUser().getUserName().trim();
        int role = UserContext.getCurrentUser().getRole();

        boolean isAdmin = role == 3;   // Admin
        boolean isSuperVisor = role == 2;  // Supervisor
        //  boolean isIbrahemMansour = username.equalsIgnoreCase("Tooling.room2");
        if (isAdmin || isSuperVisor) {
            return true;
        }
        WindowUtils.ALERT("تنبيه", "ليس للمستخدم صلاحية الوصول", WindowUtils.ALERT_ERROR);
        return false;
    }


    public static boolean adminAuthorization() {
        boolean Admin_Role = UserContext.getCurrentUser().getRole() == 3;
       // int Role = UserContext.getCurrentUser().getRole();
        if (!Admin_Role) {
            WindowUtils.ALERT("تنبيه", "ليس للمستخدم صلاحية الوصول", WindowUtils.ALERT_ERROR);
            return false;
        }
        return true;
    }


}
