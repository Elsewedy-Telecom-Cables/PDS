package com.elsewedyt.pdsapp.controllers;

import com.elsewedyt.pdsapp.dao.UserDAO;
import com.elsewedyt.pdsapp.logging.Logging;
import com.elsewedyt.pdsapp.models.User;
import com.elsewedyt.pdsapp.models.UserContext;
import com.elsewedyt.pdsapp.services.UserService;
import com.elsewedyt.pdsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import static com.elsewedyt.pdsapp.services.WindowUtils.*;

public class ViewUsersController implements Initializable {
    @FXML
    private Button add_user_btn;
    @FXML
    private Button clearSearch_btn;
    @FXML
    private TableColumn<User, String> active_colm;

    @FXML
    private TableColumn<User, String> creationDate_colm;

    @FXML
    private TableColumn<User, String> edit_colm;

    @FXML
    private TableColumn<User, String> empId_colm;

    @FXML
    private TableColumn<User, String> fullname_colm;

    @FXML
    private TableColumn<User, String> phone_colm;

    @FXML
    private TableColumn<User, String> role_colm;

    @FXML
    private TextField filterUsers_txtF;


    @FXML
    private TableColumn<User, String> userId_colm;

    @FXML
    private TableColumn<User, String> user_password_colm;

    @FXML
    private TableColumn<User, String> username_colm;

    @FXML
    private TextField users_count_textF;

    @FXML
    private Label users_num_lable;

    @FXML
    private TableView<User> users_tbl_view;

    ObservableList<User> listUsers;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> users_num_lable.requestFocus());
        userId_colm.setVisible(false);
        user_password_colm.setVisible(false);
        loadData();
        users_tbl_view.setItems(listUsers);
        // setButton Curser Hand
        add_user_btn.setCursor(Cursor.HAND);
        clearSearch_btn.setCursor(Cursor.HAND);

    }
    private void loadData() {
        Platform.runLater(() -> {
            // Fetch data
            listUsers = UserDAO.getUsers();
            if (listUsers == null) {
                Logging.logMessage(Logging.WARN, getClass().getName(), "loadData", "UserDAO.getUsers() returned null, using empty list");
                listUsers = FXCollections.observableArrayList();
            }
            users_count_textF.setText(String.valueOf(listUsers.size()));
            users_count_textF.setEditable(false);

            // Table column configurations
            userId_colm.setCellValueFactory(new PropertyValueFactory<>("userId"));
            empId_colm.setCellValueFactory(new PropertyValueFactory<>("empCode"));
            username_colm.setCellValueFactory(new PropertyValueFactory<>("userName"));
            fullname_colm.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            phone_colm.setCellValueFactory(new PropertyValueFactory<>("phone"));
            creationDate_colm.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

            role_colm.setCellValueFactory(cellData -> {
                int roleId = cellData.getValue().getRole();
                String roleStr = WindowUtils.getUserRoleStr(roleId);
                return new SimpleStringProperty(roleStr);
            });

            active_colm.setCellValueFactory(cellData -> {
                int activeVal = cellData.getValue().getActive();
                String activeStr = WindowUtils.getUserActiveStr(activeVal);
                return new SimpleStringProperty(activeStr);
            });

            // Style columns
            userId_colm.setStyle("-fx-alignment: CENTER; -fx-font-size: 13px;-fx-font-weight:bold");
            empId_colm.setStyle("-fx-alignment: CENTER; -fx-font-size: 13px;-fx-font-weight:bold");
            username_colm.setStyle("-fx-alignment: CENTER; -fx-font-size: 13px;-fx-font-weight:bold");
            user_password_colm.setStyle("-fx-alignment: CENTER; -fx-font-size: 13px;-fx-font-weight:bold");
            fullname_colm.setStyle("-fx-alignment: CENTER; -fx-font-size: 13px;-fx-font-weight:bold");
            phone_colm.setStyle("-fx-alignment: CENTER; -fx-font-size: 13px;-fx-font-weight:bold");
            role_colm.setStyle("-fx-alignment: CENTER; -fx-font-size: 13px;-fx-font-weight:bold");
            active_colm.setStyle("-fx-alignment: CENTER; -fx-font-size: 13px;-fx-font-weight:bold");
            creationDate_colm.setStyle("-fx-alignment: CENTER; -fx-font-size: 13px;-fx-font-weight:bold");
            edit_colm.setStyle("-fx-alignment: CENTER;-fx-font-weight:bold");


            // Set fixed cell size for consistent row height
            users_tbl_view.setFixedCellSize(33);


            users_tbl_view.setRowFactory(tv -> {
                TableRow<User> row = new TableRow<>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user == null || empty) {
                            setStyle(""); // Clear style for empty rows
                        } else if (isSelected()) {
                            setStyle("-fx-background-color: #b8ecf5;"); // Re-apply style if selected
                        } else {
                            setStyle(""); // Clear for non-selected rows
                        }
                    }
                };

                // Highlight selected row
                row.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    if (isSelected && !row.isEmpty()) {
                        row.setStyle("-fx-background-color: #e0f7fa;");
                    } else {
                        row.setStyle("");
                    }
                });

                return row;
            });


            // Enable single selection
            users_tbl_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            users_tbl_view.getSelectionModel().clearSelection();


            // Cell factory for edit/delete buttons
            edit_colm.setCellFactory(param -> new TableCell<User, String>() {
                private final FontIcon editIcon = new FontIcon("fa-pencil-square");
                private final FontIcon deleteIcon = new FontIcon("fa-trash");
                private final HBox manageBtn = new HBox(editIcon, deleteIcon);

                {

                    // deleteIcon.setFill(javafx.scene.paint.Color.RED);
                    manageBtn.setStyle("-fx-alignment: center;");
                    manageBtn.setSpacing(12);

                    editIcon.setCursor(Cursor.HAND);
                    editIcon.setIconSize(16);
                    editIcon.setFill(Color.GREEN);

                    deleteIcon.setCursor(Cursor.HAND);
                    deleteIcon.setIconSize(16);
                    deleteIcon.setFill(Color.RED);



                    HBox.setMargin(editIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                    HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));

                    Tooltip.install(editIcon, new Tooltip("update user"));
                    Tooltip.install(deleteIcon, new Tooltip("delete user"));

                    editIcon.setOnMouseClicked(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        if (user != null) {
                            Stage stagemain = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stagemain.close();
                            CLOSE2(event);
                            OPEN_EDIT_USER_PAGE(user.getEmpCode());
                        }
                    });

                    deleteIcon.setOnMouseClicked(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        if (user == null || user.getUserName() == null) {
                            Logging.logMessage(Logging.WARN, getClass().getName(), "deleteIcon", "User or emp_id is null");
                            WindowUtils.ALERT("ERR", "Delete user : User or emp_id is null ", WindowUtils.ALERT_WARNING);
                            return;
                        }
                        User currentUser = UserContext.getCurrentUser();
                        if (currentUser != null && currentUser.getUserName() != null && user.getUserName().equals(currentUser.getUserName())) {
                            Logging.logMessage(Logging.INFO, getClass().getName(), "deleteIcon", "Attempted self-deletion by user: " + user.getUserName());
                            WindowUtils.ALERT("ERR", "Can not delete current user", WindowUtils.ALERT_WARNING);
                            return;
                        }
                        // Proceed with deletion logic
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Confirm");
                        alert.setHeaderText("are you sure from delete this User");
                        alert.setContentText("user: " + user.getUserName() + " | " + user.getFullName());
                        ButtonType okButton = ButtonType.OK;
                        ButtonType cancelButton = ButtonType.CANCEL;
                        alert.getButtonTypes().setAll(okButton, cancelButton);

                        Button okBtn = (Button) alert.getDialogPane().lookupButton(okButton);
                        Button cancelBtn = (Button) alert.getDialogPane().lookupButton(cancelButton);
                        okBtn.setText("Ok");
                        cancelBtn.setText("Cancel");
                        Platform.runLater(cancelBtn::requestFocus);

                        alert.showAndWait().ifPresent(response -> {
                            if (response == okButton) {
                                if (UserService.confirmPassword(currentUser.getUserName())) {
                                    try {
                                        boolean deleted = UserDAO.deleteUser(user.getEmpCode());
                                        if (deleted) {
                                            loadData();
                                            WindowUtils.ALERT("Sucess", "user deleted", WindowUtils.ALERT_INFORMATION);
                                        } else {
                                            WindowUtils.ALERT("ERR", "Failed in delete user", WindowUtils.ALERT_ERROR);
                                        }
                                    } catch (Exception ex) {
                                        Logging.logException(Logging.ERROR, getClass().getName(), "deleteUser", ex);
                                        WindowUtils.ALERT("ERR", "Error during delete user", WindowUtils.ALERT_ERROR);
                                    }
                                } else {
                                    WindowUtils.ALERT("ERR", "The password is incorrect", WindowUtils.ALERT_WARNING);
                                }
                            }
                        });
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(manageBtn);
                    }
                    setText(null);
                }
            });

            users_tbl_view.setItems(listUsers);
            users_tbl_view.refresh();
        });
    }
    @FXML
    void clearSearch(ActionEvent event) {
        filterUsers_txtF.clear();
    }

    @FXML
    void filter_Users(KeyEvent event) {
        FilteredList<User> filteredData = new FilteredList<>(listUsers, p -> true);
        filterUsers_txtF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {  // object from Meals
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                String empId = user.getEmpCode() + "";
                String role = user.getRole() + "";
                String active = user.getActive() + "";
                //String username = user.getUserName() + "";
                if (user.getUserName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (user.getFullName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getCreationDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (empId.contains(lowerCaseFilter)) {
                    return true;
                } else if (role.contains(lowerCaseFilter)) {
                    return true;
                } else if (active.contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            }); // Does not match.Admin
        });
        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(users_tbl_view.comparatorProperty());
        users_tbl_view.setItems(sortedData);
    }
    @FXML
    void openAddUserPage(ActionEvent event) {
        CLOSE(event);
       OPEN_ADD_USER_PAGE();
    }

}
