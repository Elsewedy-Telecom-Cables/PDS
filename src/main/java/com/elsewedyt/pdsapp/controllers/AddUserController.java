package com.elsewedyt.pdsapp.controllers;

import com.elsewedyt.pdsapp.dao.UserDAO;
import com.elsewedyt.pdsapp.db.DEF;
import com.elsewedyt.pdsapp.logging.Logging;
import com.elsewedyt.pdsapp.models.User;
import com.elsewedyt.pdsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static com.elsewedyt.pdsapp.services.WindowUtils.*;

public class AddUserController implements Initializable {
    @FXML
    private Label page_title;

    @FXML
    private TextField emp_id_txtF;
    @FXML
    private TextField user_name_txtF;
    @FXML
    private PasswordField password_passF;
    @FXML
    private TextField full_name_txtF;
    @FXML
    private TextField phone_txtF;
    @FXML
    private ComboBox<String> userRole_ComBox;
    @FXML
    private ComboBox<String> userActive_ComBox;
    @FXML
    private Button saveUser_btn;
    int updatedUserId = 0;
    ObservableList listComboUserRole;
    ObservableList listComboUserActive;
    int indexUsers = -1;
    boolean update = false;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> page_title.requestFocus());

        listComboUserRole = FXCollections.observableArrayList(DEF.USER_ROLE_GENERAL_USER_STRING,DEF.USER_ROLE_USER_STRING,DEF.USER_ROLE_SUPERVISOR_STRING,DEF.USER_ROLE_ADMIN_STRING);
        listComboUserActive = FXCollections.observableArrayList(DEF.USER_ACTIVE_STRING,DEF.USER_NOT_ACTIVE_STRING);
        userRole_ComBox.setItems(listComboUserRole);
        userActive_ComBox.setItems(listComboUserActive);
    }

    public void setUserData(int emp_id, boolean update) {
        try {
            this.update = update;
            User us = UserDAO.getUserByEmpId(emp_id);
            emp_id_txtF.setText(us.getEmpCode() + "");
            emp_id_txtF.setEditable(false);
            user_name_txtF.setText(us.getUserName());
            user_name_txtF.setEditable(false);
            password_passF.setText(us.getPassword());
            full_name_txtF.setText(us.getFullName());
            phone_txtF.setText(us.getPhone());
           // SetUserRoleComb
            int roleId = us.getRole();
            String roleStr = WindowUtils.getUserRoleStr(roleId);
            int userRoleIndex = listComboUserRole.indexOf(roleStr);
            userRole_ComBox.getSelectionModel().select(userRoleIndex);
            // SetUserActiveComb
            int activeId = us.getActive();
            String activeStr = WindowUtils.getUserActiveStr(activeId);
            int userActiveIndex = listComboUserActive.indexOf(activeStr);
            userActive_ComBox.getSelectionModel().select(userActiveIndex);


        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "setUserData  :", ex);
        }

    }

    public void setSaveButton() {
        saveUser_btn.setText("Update");
    }
    public boolean addUserHelp() {
        try {
            // 1. Validate Employee Code (must not be empty and numeric)
            String empCodeStr = emp_id_txtF.getText();
            if (empCodeStr == null || empCodeStr.isBlank()) {
                WindowUtils.ALERT("Warning", "Employee Code is required", ALERT_INFORMATION);
                return false;
            }
            int emp_code;
            try {
                emp_code = Integer.parseInt(empCodeStr.trim());
            } catch (NumberFormatException e) {
                WindowUtils.ALERT("Warning", "Employee Code must be a number", ALERT_INFORMATION);
                return false;
            }

            // 2. Validate Text Fields
            String user_name = user_name_txtF.getText();
            String password  = password_passF.getText();
            String fullname  = full_name_txtF.getText();
            String phone     = phone_txtF.getText();

            if (user_name == null || user_name.isBlank() ||
                    password == null || password.isBlank() ||
                    fullname == null || fullname.isBlank()) {
                WindowUtils.ALERT("Warning", "User Name, Password, and Full Name are required", ALERT_INFORMATION);
                return false;
            }

            // 3. Validate Role ComboBox
            String roleStr = userRole_ComBox.getSelectionModel().getSelectedItem();
            if (roleStr == null || roleStr.isBlank()) {
                WindowUtils.ALERT("Warning", "Role is required", ALERT_INFORMATION);
                return false;
            }
            int roleInt = WindowUtils.getUserRoleInt(roleStr);

            // 4. Validate Active ComboBox
            String activeStr = userActive_ComBox.getSelectionModel().getSelectedItem();
            if (activeStr == null || activeStr.isBlank()) {
                WindowUtils.ALERT("Warning", "Active status is required", ALERT_INFORMATION);
                return false;
            }
            int activeInt = WindowUtils.getUserActiveInt(activeStr);

            // 5. Prepare user object
            Date currentDate = new Date();
            String creationDate = dateFormat.format(currentDate);

            User us = new User();
            us.setEmpCode(emp_code);
            us.setUserName(user_name.trim());
            us.setPassword(password.trim());
            us.setFullName(fullname.trim());
            us.setPhone(phone != null ? phone.trim() : "");
            us.setRole(roleInt);
            us.setActive(activeInt);
            us.setCreationDate(creationDate);

            // 6. Save to DB
            if (!UserDAO.insertUser(us)) {
                WindowUtils.ALERT("Error", "User could not be added", ALERT_INFORMATION);
                return false ;
            }
            return true; // success

        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "addUserHelp", ex);
            return false ;
        }

    }


    void clearUserPage() {
        emp_id_txtF.clear();
        user_name_txtF.clear();
        password_passF.clear();
        full_name_txtF.clear();
        phone_txtF.clear();
        userRole_ComBox.getSelectionModel().select(-1);
        userActive_ComBox.getSelectionModel().select(-1);
    }
    public boolean UpdateUserHelp() {
        try {
            // 1. Validate Employee Code
            String empCodeStr = emp_id_txtF.getText();
            if (empCodeStr == null || empCodeStr.isBlank()) {
                WindowUtils.ALERT("Warning", "Employee Code is required", ALERT_INFORMATION);
                return false;
            }

            int emp_code;
            try {
                emp_code = Integer.parseInt(empCodeStr.trim());
            } catch (NumberFormatException e) {
                WindowUtils.ALERT("Warning", "Employee Code must be a number", ALERT_INFORMATION);
                return false;
            }

            // 2. Validate Required Text Fields
            String password = password_passF.getText();
            String username = user_name_txtF.getText();
            String fullname = full_name_txtF.getText();
            String phone    = phone_txtF.getText();

            if (username == null || username.isBlank() ||
                    password == null || password.isBlank() ||
                    fullname == null || fullname.isBlank()) {
                WindowUtils.ALERT("Warning", "User Name, Password, and Full Name are required", ALERT_INFORMATION);
                return false;
            }

            // 3. Validate ComboBoxes
            String roleStr = userRole_ComBox.getSelectionModel().getSelectedItem();
            if (roleStr == null || roleStr.isBlank()) {
                WindowUtils.ALERT("Warning", "Role is required", ALERT_INFORMATION);
                return false;
            }
            int roleInt = WindowUtils.getUserRoleInt(roleStr);

            String activeStr = userActive_ComBox.getSelectionModel().getSelectedItem();
            if (activeStr == null || activeStr.isBlank()) {
                WindowUtils.ALERT("Warning", "Active status is required", ALERT_INFORMATION);
                return false;
            }
            int activeInt = WindowUtils.getUserActiveInt(activeStr);

            // 4. Create User object
            User us = new User();
            us.setEmpCode(emp_code);
            us.setUserName(username.trim());
            us.setPassword(password.trim());
            us.setFullName(fullname.trim());
            us.setPhone(phone != null ? phone.trim() : "");
            us.setRole(roleInt);
            us.setActive(activeInt);

            // 5. Update in DB
            boolean success = UserDAO.updateUser(us);
            if (!success) {
                WindowUtils.ALERT("Error", "User could not be updated", ALERT_INFORMATION);
            }
            return success;

        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "UpdateUserHelp", ex);
            return false;
        }
    }
    @FXML
    void saveUserButton(ActionEvent event) {
    try {
        if (!update) {
            boolean success = addUserHelp();
            if (success) {
                clearUserPage();
                WindowUtils.ALERT("Success", "User added successfully", WindowUtils.ALERT_INFORMATION);
            }
        } else {
            boolean success = UpdateUserHelp();
            if (success) {
                clearUserPage();
                WindowUtils.ALERT("Success", "User updated successfully", WindowUtils.ALERT_INFORMATION);
                CLOSE(event);
                OPEN_VIEW_USERS_PAGE();
            }
        }
    } catch (Exception ex) {
        Logging.logException("ERROR", this.getClass().getName(), "saveUserButton", ex);
    }
}


}
