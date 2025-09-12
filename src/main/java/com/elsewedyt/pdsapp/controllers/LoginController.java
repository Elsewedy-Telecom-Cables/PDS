package com.elsewedyt.pdsapp.controllers;

import com.elsewedyt.pdsapp.dao.UserDAO;
import com.elsewedyt.pdsapp.logging.Logging;
import com.elsewedyt.pdsapp.models.User;
import com.elsewedyt.pdsapp.models.UserContext;
import com.elsewedyt.pdsapp.services.LoggingSetting;
import com.elsewedyt.pdsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

import static com.elsewedyt.pdsapp.services.WindowUtils.*;

public class LoginController implements Initializable {

    @FXML
    private Label app_name_lable;

    @FXML
    private ImageView cal_img_view;

    @FXML
    private ImageView logo_image_view;

    @FXML
    private TextField user_name_txtF;

    @FXML
    private PasswordField password_passF;
    @FXML
    private Button sign_in_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> password_passF.requestFocus());
        Image logoImg = new Image(LoginController.class.getResourceAsStream("/images/company_logo.png"));
        logo_image_view.setImage(logoImg);
        sign_in_btn.setCursor((Cursor.HAND));
        user_name_txtF.setText(LoggingSetting.getCurrentUsername());

    }

    @FXML
    void login(ActionEvent event) {
        String username = user_name_txtF.getText().trim();
        String password = password_passF.getText().trim();


        if (username.isEmpty() || password.isEmpty()) {
            WindowUtils.ALERT("ERROR", "Please enter username and password", WindowUtils.ALERT_WARNING);
            return;
        }

        User user = UserDAO.getUserByUsername(username);

        if (user == null) {
            WindowUtils.ALERT("ERROR", "Error in user name", WindowUtils.ALERT_WARNING);
            return;
        }

        if (!user.getPassword().equals(password)) {
            WindowUtils.ALERT("ERROR", "Error in password", WindowUtils.ALERT_WARNING);
            return;
        }
        if (user.getActive() == 0) {
            WindowUtils.ALERT("ERROR", "This User Not Active", WindowUtils.ALERT_WARNING);
            return;
        }

        try {
            UserContext.setCurrentUser(user);
            //  LoggingSetting.saveLastUsername(username);
            CLOSE(event);
            OPEN_MAIN_PAGE();
        } catch (Exception ex) {
            WindowUtils.ALERT("ERROR", "An unexpected error occurred", WindowUtils.ALERT_WARNING);
            Logging.logException("ERROR", this.getClass().getName(), "login", ex);
        }

    }

    @FXML
    void enterLogin(ActionEvent event) {
        login(event);
    }

}

