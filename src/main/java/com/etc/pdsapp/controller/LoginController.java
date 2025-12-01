package com.etc.pdsapp.controller;

import com.etc.pdsapp.dao.AppContext;
import com.etc.pdsapp.dao.UserDao;
import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.model.User;
import com.etc.pdsapp.model.UserContext;
import com.etc.pdsapp.services.LoggingSetting;
import com.etc.pdsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.etc.pdsapp.services.WindowUtils.*;

public class LoginController implements Initializable {

    @FXML private ImageView logoImageView;

    @FXML private TextField userNameTxtF;

    @FXML private PasswordField passwordPassF;


   private final UserDao userDao = AppContext.getInstance().getUserDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> passwordPassF.requestFocus());
        Image logoImg = new Image(Objects.requireNonNull(LoginController.class.getResourceAsStream("/images/etc_logo.png")));
        logoImageView.setImage(logoImg);
        userNameTxtF.setText(LoggingSetting.getCurrentUsername());

    }

    @FXML
    void login(ActionEvent event) {
        String username = userNameTxtF.getText().trim();
        String password = passwordPassF.getText().trim();


        if (username.isEmpty() || password.isEmpty()) {
            WindowUtils.ALERT("ERROR", "Please enter username and password", WindowUtils.ALERT_WARNING);
            return;
        }

        User user = userDao.getUserByUsername(username);

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

