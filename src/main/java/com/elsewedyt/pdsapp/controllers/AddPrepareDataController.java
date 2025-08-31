
package com.elsewedyt.pdsapp.controllers;

import com.elsewedyt.pdsapp.dao.MachineDAO;
import com.elsewedyt.pdsapp.dao.SectionDAO;
import com.elsewedyt.pdsapp.dao.SupplierDAO;
import com.elsewedyt.pdsapp.logging.Logging;
import com.elsewedyt.pdsapp.models.Machine;
import com.elsewedyt.pdsapp.models.Section;
import com.elsewedyt.pdsapp.models.Supplier;
import com.elsewedyt.pdsapp.models.UserContext;
import com.elsewedyt.pdsapp.services.UserService;
import com.elsewedyt.pdsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class AddPrepareDataController implements Initializable {
    @FXML private TableView<Machine> table1 ;
    @FXML private TableView<Section> table2 ;
    @FXML private TableView<Supplier> table4 ;

    @FXML private Button add_machine_btn;
    @FXML private Button add_section_btn;
    @FXML private Button add_supplier_btn;
    @FXML private Button clear_machine_btn;
    @FXML private Button clear_section_btn;
    @FXML private Button clear_supplier_btn;
    @FXML private Label date_lbl;

    @FXML private TextField filter_machines_textF;
    @FXML private TextField filter_section_textF;
    @FXML private TextField filter_supplier_textF;
    @FXML private ImageView logo_ImageView;
    @FXML private TableColumn<Machine, String> machine_delete_colm;
    @FXML private TableColumn<Machine, Integer> machine_id_colm;
    @FXML private TableColumn<Machine, String> machine_name_colm;
    @FXML private TextField machine_name_textF;
    @FXML private TableView<Machine> machines_table_view;
    @FXML private TableColumn<Section, String> section_delete_colm;
    @FXML private TableColumn<Section, Integer> section_id_colm;
    @FXML private TableColumn<Section, String> section_name_colm;
    @FXML private TextField section_name_textF;
    @FXML private TableView<Section> section_table_view;
    @FXML private TableColumn<Supplier, String> supplier_delete_colm;
    @FXML private TableColumn<Supplier, Integer> supplier_id_colm;
    @FXML private TableColumn<Supplier, String> supplier_name_colm;
    @FXML private TextField supplier_name_textF;
    @FXML private TableView<Supplier> supplier_table_view;
    @FXML private Button update_machine_btn;
    @FXML private TextField update_machine_name_textF;
    @FXML private Button update_section_btn;
    @FXML private TextField update_section_name_textF;
    @FXML private Button update_supplier_btn;
    @FXML private TextField update_supplier_name_textF;
    @FXML private Label welcome_lbl;

    ObservableList<Machine> machineList;
    ObservableList<Section> sectionList;
    ObservableList<Supplier> supplierList;


    // DAO instances
    private final MachineDAO machineDAO = new MachineDAO();
    private final SectionDAO sectionDAO = new SectionDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> welcome_lbl.requestFocus());

        // Load and set company logo
        Image img = new Image(getClass().getResourceAsStream("/images/company_logo.png"));
        logo_ImageView.setImage(img);

        // Set current date and time
        java.util.Date date = new java.util.Date();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy  hh:mm a");
        date_lbl.setText(dateFormat2.format(date) + " ");

        // Set welcome message with current user's full name
        String msg = "Welcome : " + UserContext.getCurrentUser().getFullName();
        welcome_lbl.setText(msg);

        // Set cursor to hand for all buttons
        add_machine_btn.setCursor(Cursor.HAND);
        add_section_btn.setCursor(Cursor.HAND);
        add_supplier_btn.setCursor(Cursor.HAND);
        clear_machine_btn.setCursor(Cursor.HAND);
        clear_section_btn.setCursor(Cursor.HAND);
        clear_supplier_btn.setCursor(Cursor.HAND);
        update_machine_btn.setCursor(Cursor.HAND);
        update_section_btn.setCursor(Cursor.HAND);
        update_supplier_btn.setCursor(Cursor.HAND);

        // Load Data For All Tables
        loadMachinesData();
        loadSectionsData();
        loadSuppliersData();

        // Initialize ObservableLists
        machineList = machineDAO.getAllMachines();
        sectionList = sectionDAO.getAllSections();
        supplierList = supplierDAO.getAllSuppliers();


        // Set items to TableViews
        machines_table_view.setItems(machineList);
        section_table_view.setItems(sectionList);
        supplier_table_view.setItems(supplierList);

        // Call Tables Listener
        setupMachineTableListener();
        setupSectionTableListener();
        setupSupplierTableListener();

    }

    // Load Machines Data
    private void loadMachinesData() {
        machine_name_colm.setCellValueFactory(new PropertyValueFactory<>("machineName"));
        machine_id_colm.setCellValueFactory(new PropertyValueFactory<>("machineId"));
        machine_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:11 px;-fx-font-weight:bold;");
        machine_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Machine, String>, TableCell<Machine, String>> cellFactory = param -> {
            final TableCell<Machine, String> cell = new TableCell<Machine, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete Machine"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this machine?");
                            alert.setContentText("Delete machine confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(() -> cancelButton.requestFocus());
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            Machine machine = machines_table_view.getSelectionModel().getSelectedItem();
                                            machineDAO.deleteMachine(machine.getMachineId());
                                            machineList = machineDAO.getAllMachines();
                                            machines_table_view.setItems(machineList);
                                            WindowUtils.ALERT("Success", "Machine deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteMachine", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        machine_delete_colm.setCellFactory(cellFactory);
        machines_table_view.setItems(machineList);
    }

    // Load Sections Data
    private void loadSectionsData() {
        section_name_colm.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
        section_id_colm.setCellValueFactory(new PropertyValueFactory<>("sectionId"));
        section_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        section_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Section, String>, TableCell<Section, String>> cellFactory = param -> {
            final TableCell<Section, String> cell = new TableCell<Section, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete Section"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this section?");
                            alert.setContentText("Delete section confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(() -> cancelButton.requestFocus());
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            Section section = section_table_view.getSelectionModel().getSelectedItem();
                                            sectionDAO.deleteSection(section.getSectionId());
                                            sectionList = sectionDAO.getAllSections();
                                            section_table_view.setItems(sectionList);
                                            WindowUtils.ALERT("Success", "Section deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteSection", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        section_delete_colm.setCellFactory(cellFactory);
        section_table_view.setItems(sectionList);
    }

    // Load Locations Data

    // Load Suppliers Data
    private void loadSuppliersData() {
        supplier_name_colm.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supplier_id_colm.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        supplier_name_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        supplier_id_colm.setStyle("-fx-alignment: CENTER;-fx-font-size:12 px;-fx-font-weight:bold;");
        Callback<TableColumn<Supplier, String>, TableCell<Supplier, String>> cellFactory = param -> {
            final TableCell<Supplier, String> cell = new TableCell<Supplier, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final FontIcon deleteIcon = new FontIcon("fas-trash");
                        deleteIcon.setCursor(Cursor.HAND);
                        deleteIcon.setIconSize(13);
                        deleteIcon.setFill(javafx.scene.paint.Color.RED);
                        Tooltip.install(deleteIcon, new Tooltip("Delete Supplier"));

                        deleteIcon.setOnMouseClicked(event -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Are you sure you want to delete this supplier?");
                            alert.setContentText("Delete supplier confirmation");
                            alert.getButtonTypes().addAll(ButtonType.CANCEL);

                            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                            cancelButton.setText("Cancel");
                            okButton.setText("OK");
                            Platform.runLater(() -> cancelButton.requestFocus());
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    if (UserService.confirmPassword(UserContext.getCurrentUser().getUserName())) {
                                        try {
                                            Supplier supplier = supplier_table_view.getSelectionModel().getSelectedItem();
                                            supplierDAO.deleteSupplier(supplier.getSupplierId());
                                            supplierList = supplierDAO.getAllSuppliers();
                                            supplier_table_view.setItems(supplierList);
                                            WindowUtils.ALERT("Success", "Supplier deleted successfully", WindowUtils.ALERT_INFORMATION);
                                        } catch (Exception ex) {
                                            Logging.logException("ERROR", getClass().getName(), "deleteSupplier", ex);
                                        }
                                    } else {
                                        WindowUtils.ALERT("ERR", "Password not correct", WindowUtils.ALERT_WARNING);
                                    }
                                }
                            });
                        });

                        HBox manageBtn = new HBox(deleteIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new javafx.geometry.Insets(2, 2, 0, 3));
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        supplier_delete_colm.setCellFactory(cellFactory);
        supplier_table_view.setItems(supplierList);
    }

    // Filter Machines
    @FXML
    void filter_machines(KeyEvent event) {
        FilteredList<Machine> filteredData = new FilteredList<>(machineList, p -> true);
        filter_machines_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(machine -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (machine.getMachineName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = machine.getMachineId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Machine> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(machines_table_view.comparatorProperty());
        machines_table_view.setItems(sortedData);
    }

    // Filter Sections
    @FXML
    void filter_section(KeyEvent event) {
        FilteredList<Section> filteredData = new FilteredList<>(sectionList, p -> true);
        filter_section_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(section -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (section.getSectionName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = section.getSectionId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Section> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(section_table_view.comparatorProperty());
        section_table_view.setItems(sortedData);
    }

    // Filter Suppliers
    @FXML
    void filter_supplier(KeyEvent event) {
        FilteredList<Supplier> filteredData = new FilteredList<>(supplierList, p -> true);
        filter_supplier_textF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplier.getSupplierName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                String id = supplier.getSupplierId() + "";
                return id.contains(lowerCaseFilter);
            });
        });
        SortedList<Supplier> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(supplier_table_view.comparatorProperty());
        supplier_table_view.setItems(sortedData);
    }


    // Add Machine
    @FXML
    void add_machine(ActionEvent event) {
        String machineName = machine_name_textF.getText().trim();
        if (machineName.isEmpty()) {
            WindowUtils.ALERT("ERR", "machine_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Machine machine = new Machine();
        machine.setMachineName(machineName);

        boolean success = MachineDAO.insertMachine(machine);

        if (success) {
            WindowUtils.ALERT("Success", "Machine added successfully", WindowUtils.ALERT_INFORMATION);
            machine_name_textF.clear();
            filter_machines_textF.clear();
            update_machine_name_textF.clear();
            machineList = machineDAO.getAllMachines();
            machines_table_view.setItems(machineList);
        } else {
            WindowUtils.ALERT("database_error", "machine_add_failed", WindowUtils.ALERT_ERROR);
        }
    }

    // Add Section
    @FXML
    void add_section(ActionEvent event) {
        String sectionName = section_name_textF.getText().trim();
        if (sectionName.isEmpty()) {
            WindowUtils.ALERT("ERR", "section_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Section section = new Section();
        section.setSectionName(sectionName);

        int generatedId = sectionDAO.insertSection(section);

        if (generatedId != -1) {
            WindowUtils.ALERT("Success", "Section added successfully", WindowUtils.ALERT_INFORMATION);
            section_name_textF.clear();
            update_section_name_textF.clear();
            filter_section_textF.clear();
            sectionList = sectionDAO.getAllSections();
            section_table_view.setItems(sectionList);
        } else {
            WindowUtils.ALERT("database_error", "section_add_failed", WindowUtils.ALERT_ERROR);
        }
    }

    // Add Supplier
    @FXML
    void add_supplier(ActionEvent event) {
        String supplierName = supplier_name_textF.getText().trim();
        if (supplierName.isEmpty()) {
            WindowUtils.ALERT("ERR", "supplier_name_empty", WindowUtils.ALERT_ERROR);
            return;
        }

        Supplier supplier = new Supplier();
        supplier.setSupplierName(supplierName);

        boolean sucess = supplierDAO.insertSupplier(supplier);

        if (sucess) {
            WindowUtils.ALERT("Success", "Supplier added successfully", WindowUtils.ALERT_INFORMATION);
            supplier_name_textF.clear();
            update_supplier_name_textF.clear();
            filter_supplier_textF.clear();
            supplierList = supplierDAO.getAllSuppliers();
            supplier_table_view.setItems(supplierList);
        } else {
            WindowUtils.ALERT("database_error", "supplier_add_failed", WindowUtils.ALERT_ERROR);
        }
    }


    // Update Machine
    @FXML
    void update_machine(ActionEvent event) {
        try {
            Machine selectedMachine = machines_table_view.getSelectionModel().getSelectedItem();
            if (selectedMachine == null) {
                WindowUtils.ALERT("ERR", "No Machine selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String machineName = update_machine_name_textF.getText().trim();
            if (machineName.isEmpty()) {
                WindowUtils.ALERT("ERR", "machine_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedMachine.setMachineName(machineName);
            boolean success = machineDAO.updateMachine(selectedMachine);
            if (success) {
                WindowUtils.ALERT("Success", "Machine updated successfully", WindowUtils.ALERT_INFORMATION);
                update_machine_name_textF.clear();
                filter_machines_textF.clear();
                machine_name_textF.clear();
                machineList = machineDAO.getAllMachines();
                machines_table_view.setItems(machineList);
            } else {
                WindowUtils.ALERT("ERR", "machine_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateMachine", ex);
        }
    }

    // Update Section
    @FXML
    void update_section(ActionEvent event) {
        try {
            Section selectedSection = section_table_view.getSelectionModel().getSelectedItem();
            if (selectedSection == null) {
                WindowUtils.ALERT("ERR", "No Section selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String sectionName = update_section_name_textF.getText().trim();
            if (sectionName.isEmpty()) {
                WindowUtils.ALERT("ERR", "section_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedSection.setSectionName(sectionName);
            boolean success = sectionDAO.updateSection(selectedSection);
            if (success) {
                WindowUtils.ALERT("Success", "Section updated successfully", WindowUtils.ALERT_INFORMATION);
                update_section_name_textF.clear();
                section_name_textF.clear();
                filter_section_textF.clear();

                sectionList = sectionDAO.getAllSections();
                section_table_view.setItems(sectionList);
            } else {
                WindowUtils.ALERT("ERR", "section_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateSection", ex);
        }
    }


    // Update Supplier
    @FXML
    void update_supplier(ActionEvent event) {
        try {
            Supplier selectedSupplier = supplier_table_view.getSelectionModel().getSelectedItem();
            if (selectedSupplier == null) {
                WindowUtils.ALERT("ERR", "No Supplier selected", WindowUtils.ALERT_ERROR);
                return;
            }

            String supplierName = update_supplier_name_textF.getText().trim();
            if (supplierName.isEmpty()) {
                WindowUtils.ALERT("ERR", "supplier_name_empty", WindowUtils.ALERT_ERROR);
                return;
            }

            selectedSupplier.setSupplierName(supplierName);
            boolean success = supplierDAO.updateSupplier(selectedSupplier);
            if (success) {
                WindowUtils.ALERT("Success", "Supplier updated successfully", WindowUtils.ALERT_INFORMATION);
                update_supplier_name_textF.clear();
                update_supplier_name_textF.clear();
                filter_supplier_textF.clear();
                supplierList = supplierDAO.getAllSuppliers();
                supplier_table_view.setItems(supplierList);
            } else {
                WindowUtils.ALERT("ERR", "supplier_updated_failed", WindowUtils.ALERT_ERROR);
            }
        } catch (Exception ex) {
            Logging.logException("ERROR", getClass().getName(), "updateSupplier", ex);
        }
    }


    // Clear Machine
    @FXML
    void clear_machine(ActionEvent event) {
        filter_machines_textF.clear();
        update_machine_name_textF.clear();
        machine_name_textF.clear();
    }

    // Clear Section
    @FXML
    void clear_section(ActionEvent event) {
        filter_section_textF.clear();
        update_section_name_textF.clear();
        section_name_textF.clear();
    }

    // Clear Supplier
    @FXML
    void clear_supplier(ActionEvent event) {
        filter_supplier_textF.clear();
        update_supplier_name_textF.clear();
        supplier_name_textF.clear();
    }

    // Setup Machine Table Listener
    private void setupMachineTableListener() {
        machines_table_view.setOnMouseClicked(event -> {
            Machine selectedMachine = machines_table_view.getSelectionModel().getSelectedItem();
            if (selectedMachine != null) {
                update_machine_name_textF.setText(selectedMachine.getMachineName());
            }
        });
    }

    // Setup Section Table Listener
    private void setupSectionTableListener() {
        section_table_view.setOnMouseClicked(event -> {
            Section selectedSection = section_table_view.getSelectionModel().getSelectedItem();
            if (selectedSection != null) {
                update_section_name_textF.setText(selectedSection.getSectionName());
            }
        });
    }

    // Setup Supplier Table Listener
    private void setupSupplierTableListener() {
        supplier_table_view.setOnMouseClicked(event -> {
            Supplier selectedSupplier = supplier_table_view.getSelectionModel().getSelectedItem();
            if (selectedSupplier != null) {
                update_supplier_name_textF.setText(selectedSupplier.getSupplierName());
            }
        });
    }

}

