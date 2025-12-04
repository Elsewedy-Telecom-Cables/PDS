package com.etc.pdsapp.controller;

import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.model.UserContext;
import com.etc.pdsapp.services.ShiftManager;
import com.etc.pdsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.etc.pdsapp.services.WindowUtils.*;

public class MainController implements Initializable {

    @FXML private Label date_lbl;

    @FXML private Label shift_label;

    @FXML private Label welcome_lbl;

    @FXML private Button users_btn;

    @FXML private ImageView logo_ImageView;

    @FXML private ImageView pds_image_view;

    @FXML private Button prepare_data_btn;

    @FXML private Button addPdsData_btn;
    @FXML private Button exportPds_btn;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // Set focus to welcome label
        Platform.runLater(() -> welcome_lbl.requestFocus());

        // Set shift information
        ShiftManager.setSHIFT(LocalDateTime.now());
        String shiftName = ShiftManager.SHIFT_NAME;
        shift_label.setText("Shift : " + shiftName);



        // Load and set company logo
        Image img = new Image(Objects.requireNonNull(MainController.class.getResourceAsStream("/images/etc_logo.png")));
        logo_ImageView.setImage(img);
        Image img2 = new Image(Objects.requireNonNull(MainController.class.getResourceAsStream("/images/pdshome.png")));
        pds_image_view.setImage(img2);
        // Set welcome message with current user's full name
        String msg = ("Welcome : " + UserContext.getCurrentUser().getFullName());
        welcome_lbl.setText(msg);

        // Set current date and time
        java.util.Date date = new java.util.Date();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy  hh:mm a");
        date_lbl.setText(dateFormat2.format(date) + " ");

        //set Curser
        users_btn.setCursor(Cursor.HAND);
        prepare_data_btn.setCursor(Cursor.HAND);


    }


    @FXML
    void openViewUsers(ActionEvent event) {
        // set Permissions
        try {
            // Super Admin and Department Manager
            int role = UserContext.getCurrentUser().getRole();
            if (role == 3) {
                CLOSE(event);
                OPEN_VIEW_USERS_PAGE();
            } else {
                WindowUtils.ALERT("Warning", "You are not authorized to access this page.", WindowUtils.ALERT_WARNING);
                return;
            }
        }catch (Exception ex){
            Logging.logException("ERROR", this.getClass().getName(), "openPrepareData Permission", ex);
        }

    }

    @FXML
    void openPrepareData(ActionEvent event) {
                    CLOSE(event);
                    OPEN_PREPARE_DATA_PAGE();
        }

    @FXML
    void openAddPdsData(ActionEvent event) {
        CLOSE(event);
        OPEN_ADD_UPDATE_PDS_DATA();
    }

    @FXML
    void openExportPds(ActionEvent event) {
         CLOSE(event);
         OPEN_EXPORT_PDS_DATA();

    }





}
