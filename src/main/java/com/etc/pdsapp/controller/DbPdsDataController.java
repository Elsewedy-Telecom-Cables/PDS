package com.etc.pdsapp.controller;
import com.etc.pdsapp.dao.*;
import com.etc.pdsapp.enums.StageType;
import com.etc.pdsapp.excel.core.StageExcelService;
import com.etc.pdsapp.excel.core.StageProcessor;
import com.etc.pdsapp.excel.core.StageValidator;
import com.etc.pdsapp.excel.file.AssemblyFile;
import com.etc.pdsapp.excel.file.BraidFile;
import com.etc.pdsapp.excel.service.AssemblyExcelHandler;
import com.etc.pdsapp.excel.service.BraidExcelHandler;
import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.model.*;
import com.etc.pdsapp.services.ApiCaller;
import com.etc.pdsapp.services.ConfigLoader;
import com.etc.pdsapp.services.WindowUtils;
import com.etc.pdsapp.utils.StageUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class DbPdsDataController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private TextField stageDescriptionTextF;
    @FXML private Label woLabel;
    @FXML private TextField workOrderTextF;
    @FXML private Button exportExcelBtn;
    @FXML private Button importExcelBtn;
    @FXML private ComboBox<Machine> machinesCombo;
    @FXML private ComboBox<Stage> stagesCombo;

    // Lists
    ObservableList<Stage> listStages;
    ObservableList<Machine> listMachines;

    private final AssemblyDao assemblyDao = AppContext.getInstance().getAssemblyDao();
    private final BraidDao braidDao = AppContext.getInstance().getBraidDao();
    private final MachineDao machineDao = AppContext.getInstance().getMachineDao();
    private final StageDao stageDao = AppContext.getInstance().getStageDao();
    private final UserDao userDao = AppContext.getInstance().getUserDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      //  Platform.runLater(() -> woLabel.requestFocus());
        workOrderTextF.setText("STC-2025-");
        Platform.runLater(() -> {
            workOrderTextF.requestFocus();
            workOrderTextF.positionCaret(workOrderTextF.getText().length());
        });
        int userId = UserContext.getCurrentUser().getUserId();
        initCombo();
        initExcelIcon();
    }

    private String normalize(String s) {
        return s
                .toLowerCase()
                .replaceAll("[\\s]", "")        // Remove all spaces
                .replaceAll("[\\[\\]\\(\\){}]", "") // Remove Brackets
                .replaceAll("[^a-z0-9]", "");   // Remove all non-alphanumeric characters
    }


    @FXML
    void saveRecord(ActionEvent event) {
        try {
            int userId = UserContext.getCurrentUser().getUserId();
            Stage selectedStage = stagesCombo.getSelectionModel().getSelectedItem();
            Machine selectedMachine = machinesCombo.getSelectionModel().getSelectedItem();
            String stageDesc = stageDescriptionTextF.getText().trim();

            if (selectedStage == null || selectedMachine == null || stageDesc.isEmpty()) {
                WindowUtils.ALERT("Warning", "Stage, Machine, and Description are required", WindowUtils.ALERT_WARNING);
                return;
            }

            String normalizedDesc = StageUtils.normalize(stageDesc);
            StageType stageType = StageType.fromId(selectedStage.getStageId());

            if (stageType == null) {
                WindowUtils.ALERT("Info", "This stage is not supported yet", WindowUtils.ALERT_INFORMATION);
                return;
            }

            // Generic duplicate check using the shared logic
            boolean exists = switch (stageType) {
                case ASSEMBLY -> assemblyDao.existsAssemblyRecord(normalizedDesc, selectedMachine.getMachineId());
                case BRAID     -> braidDao.existsBraidRecord(normalizedDesc, selectedMachine.getMachineId());
                default -> false; // Other stages not supported yet for saving
            };

            if (exists) {
                WindowUtils.ALERT("Error", "This combination of Stage & Machine already exists!", WindowUtils.ALERT_ERROR);
                return;
            }

            // Generic insert
            boolean success = switch (stageType) {
                case ASSEMBLY -> {
                    Assembly a = new Assembly(stageDesc, selectedMachine.getMachineId(), userId);
                    yield assemblyDao.insert(a) > 0;
                }
                case BRAID -> {
                    Braid b = new Braid(stageDesc, selectedMachine.getMachineId(), userId);
                    yield braidDao.insertBraid(b) > 0;
                }
                default -> false;
            };

            if (success) {
                WindowUtils.ALERT("Success", stageType.getDisplayName() + " record saved successfully", WindowUtils.ALERT_INFORMATION);
                clearSearchWorkOrder();
                machinesCombo.getSelectionModel().clearSelection();
                stageDescriptionTextF.clear();
            } else {
                WindowUtils.ALERT("Error", "Failed to save " + stageType.getDisplayName() + " record", WindowUtils.ALERT_ERROR);
            }

        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "saveRecord", ex);
            WindowUtils.ALERT("Error", "Unexpected error while saving record", WindowUtils.ALERT_ERROR);
        }
    }

    @FXML
    void exportExcel(ActionEvent event) {
        try {
            Stage selectedStage = stagesCombo.getSelectionModel().getSelectedItem();
            if (selectedStage == null) {
                WindowUtils.ALERT("Warning", "Please select a stage first", WindowUtils.ALERT_WARNING);
                return;
            }

            StageType stageType = StageType.fromId(selectedStage.getStageId());
            if (stageType == null) {
                WindowUtils.ALERT("Info", "Stage not supported for export yet", WindowUtils.ALERT_INFORMATION);
                return;
            }

            switch (stageType) {
                case ASSEMBLY -> AssemblyFile.exportToExcel(assemblyDao.getAll());
                case BRAID    -> BraidFile.exportToExcel(braidDao.getAllBraids());
                default -> WindowUtils.ALERT("Coming Soon", stageType.getDisplayName() + " export will be available soon", WindowUtils.ALERT_INFORMATION);
            }

        } catch (Exception e) {
            Logging.logException("ERROR", this.getClass().getName(), "exportExcel", e);
            WindowUtils.ALERT("Error", "Export failed: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }

    @FXML
    void importExcel(ActionEvent event) {
        try {
            Stage selectedStage = stagesCombo.getSelectionModel().getSelectedItem();
            if (selectedStage == null) {
                WindowUtils.ALERT("Warning", "Please select a stage first", WindowUtils.ALERT_WARNING);
                return;
            }

            StageType stageType = StageType.fromId(selectedStage.getStageId());
            if (stageType == null) {
                WindowUtils.ALERT("Not Supported", "This stage is not supported yet", WindowUtils.ALERT_INFORMATION);
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Excel File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showOpenDialog(null);
            if (file == null) return;

            Object handler = switch (stageType) {
                case ASSEMBLY -> new AssemblyExcelHandler();
                case BRAID    -> new BraidExcelHandler();
                default -> null;
            };

            if (handler == null) {
                WindowUtils.ALERT("Coming Soon", stageType.getDisplayName() + " import will be available soon", WindowUtils.ALERT_INFORMATION);
                return;
            }

            @SuppressWarnings("unchecked")
            var excelService = (StageExcelService<Object>) handler;
            @SuppressWarnings("unchecked")
            var validator = (StageValidator<Object>) handler;
            @SuppressWarnings("unchecked")
            var processor = (StageProcessor<Object>) handler;

            List<Object> data = excelService.importFromExcel(file);
            List<String> errors = validator.validate(data);

            if (!errors.isEmpty()) {
                WindowUtils.ALERT("Validation Errors", String.join("\n", errors), WindowUtils.ALERT_ERROR);
                return;
            }

            boolean[] hasError = new boolean[1];
            processor.applyActions(data, hasError);

            if (!hasError[0]) {
                WindowUtils.ALERT("Success", stageType.getDisplayName() + " data imported and applied successfully", WindowUtils.ALERT_INFORMATION);
            } else {
                WindowUtils.ALERT("Completed with Warnings", "Some records were skipped or failed (see previous messages)", WindowUtils.ALERT_WARNING);
            }

        } catch (IOException e) {
            Logging.logException("ERROR", getClass().getName(), "importExcel", e);
            WindowUtils.ALERT("Import Failed", "Failed to read Excel file.\nDetails:\n" + e.getMessage(), WindowUtils.ALERT_ERROR);
        } catch (Exception e) {
            Logging.logException("ERROR", getClass().getName(), "importExcel", e);
            WindowUtils.ALERT("Error", "Unexpected error: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }



//    //  Saved - Export - Import
//    @FXML
//    void saveRecord(ActionEvent event) {
//        try {
//            int userId = UserContext.getCurrentUser().getUserId();
//            Stage selectedStage = stagesCombo.getSelectionModel().getSelectedItem();
//            Machine selectedMachine = machinesCombo.getSelectionModel().getSelectedItem();
//            String stageDesc = stageDescriptionTextF.getText();
//            String normalizedDesc = normalize(stageDesc);
//
//            if (selectedStage == null || selectedMachine == null || normalizedDesc.isEmpty()) {
//                WindowUtils.ALERT("Warning", "Stage, Machine, and Description are required", WindowUtils.ALERT_WARNING);
//                return;
//            }
//
//            // Use switch based on stage ID (currently only handling Assembly, extend later)
//            switch (selectedStage.getStageId()) {
//                case 2: // Assembly
//                    // Check duplicate
//                    if (assemblyDao.existsAssemblyRecord(normalizedDesc, selectedMachine.getMachineId())) {
//                        WindowUtils.ALERT("Error", "This combination of Stage & Machine already exists!", WindowUtils.ALERT_ERROR);
//                        return;
//                    }
//
//                    // Insert new Assembly row
//                    Assembly newAssembly = new Assembly();
//                    newAssembly.setStageDescription(stageDesc);
//                    newAssembly.setMachineId(selectedMachine.getMachineId());
//                    newAssembly.setUserId(userId);
//
//                    int generatedId = assemblyDao.insert(newAssembly);
//                    if (generatedId != -1) {
//                        WindowUtils.ALERT("Success", "Assembly record saved successfully", WindowUtils.ALERT_INFORMATION);
//                        clearSearchWorkOrder();
//                        machinesCombo.getSelectionModel().clearSelection();
//                    } else {
//                        WindowUtils.ALERT("Error", "Failed to save Assembly record", WindowUtils.ALERT_ERROR);
//                    }
//                    break;
//                case 4: // Braid
//                    // Check duplicate
//                    if (braidDao.existsBraidRecord(normalizedDesc, selectedMachine.getMachineId())) {
//                        WindowUtils.ALERT("Error", "This combination of Stage & Machine already exists!", WindowUtils.ALERT_ERROR);
//                        return;
//                    }
//
//                    // Insert new Braid row
//                    Braid newBraid = new Braid();
//                    newBraid.setStageDescription(stageDesc);
//                    newBraid.setMachineId(selectedMachine.getMachineId());
//                    newBraid.setUserId(userId);
//
//                    int braidGeneratedId = braidDao.insertBraid(newBraid);
//                    if (braidGeneratedId != -1) {
//                        WindowUtils.ALERT("Success", "Braid record saved successfully", WindowUtils.ALERT_INFORMATION);
//                        clearSearchWorkOrder();
//                        machinesCombo.getSelectionModel().clearSelection();
//                    } else {
//                        WindowUtils.ALERT("Error", "Failed to save Braid record", WindowUtils.ALERT_ERROR);
//                    }
//                    break;
//                // Add cases for other stages like case 1: // Insulation, etc., with their respective DAOs
//                default:
//                    WindowUtils.ALERT("Info", "Stage not yet supported for saving", WindowUtils.ALERT_INFORMATION);
//                    break;
//            }
//
//        } catch (Exception ex) {
//            Logging.logException("ERROR", this.getClass().getName(), "saveAssemblyRecord", ex);
//        }
//    }
//
//    @FXML
//    void exportExcel(ActionEvent event) {
//        try {
//            Stage selectedStage = stagesCombo.getSelectionModel().getSelectedItem();
//            if (selectedStage == null) {
//                WindowUtils.ALERT("Warning", "Please select a stage first", WindowUtils.ALERT_WARNING);
//                return;
//            }
//
//            StageType stageType = StageType.fromId(selectedStage.getStageId());
//            if (stageType == null) {
//                WindowUtils.ALERT("Info", "Stage not yet supported for export", WindowUtils.ALERT_INFORMATION);
//                return;
//            }
//
//            switch (stageType) {
//                case ASSEMBLY -> {
//                    List<Assembly> assemblies = assemblyDao.getAll();
//                    AssemblyFile.exportToExcel(assemblies);
//                }
//                case BRAID -> {
//                    List<Braid> braids = braidDao.getAllBraids();
//                    BraidFile.exportToExcel(braids);
//                }
//                default -> WindowUtils.ALERT("Info", stageType.getDisplayName() + " export coming soon", WindowUtils.ALERT_INFORMATION);
//            }
//        } catch (Exception e) {
//            Logging.logException("ERROR", this.getClass().getName(), "exportExcel", e);
//            WindowUtils.ALERT("Error", "Export failed: " + e.getMessage(), WindowUtils.ALERT_ERROR);
//        }
//    }
//
//    @FXML
//    void importExcel(ActionEvent event) {
//        try {
//            Stage selectedStage = stagesCombo.getSelectionModel().getSelectedItem();
//            if (selectedStage == null) {
//                WindowUtils.ALERT("Warning", "Please select a stage first", WindowUtils.ALERT_WARNING);
//                return;
//            }
//
//            StageType stageType = StageType.fromId(selectedStage.getStageId());
//            if (stageType == null) {
//                WindowUtils.ALERT("Not Supported", "This stage is not supported yet", WindowUtils.ALERT_INFORMATION);
//                return;
//            }
//
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Open Excel File");
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
//            File file = fileChooser.showOpenDialog(null);
//            if (file == null) return;
//
//            Object handler = switch (stageType) {
//                case ASSEMBLY -> new AssemblyExcelHandler();
//                case BRAID    -> new BraidExcelHandler();
//                default -> null;
//            };
//
//            if (handler == null) {
//                WindowUtils.ALERT("Coming Soon", stageType.getDisplayName() + " import will be available soon", WindowUtils.ALERT_INFORMATION);
//                return;
//            }
//
//            @SuppressWarnings("unchecked")
//            var excelService = (StageExcelService<Object>) handler;
//
//            @SuppressWarnings("unchecked")
//            var validator = (StageValidator<Object>) handler;
//
//            @SuppressWarnings("unchecked")
//            var processor = (StageProcessor<Object>) handler;
//
//            List<Object> data = excelService.importFromExcel(file);
//            List<String> errors = validator.validate(data);
//
//            if (!errors.isEmpty()) {
//                WindowUtils.ALERT("Validation Errors", String.join("\n", errors), WindowUtils.ALERT_ERROR);
//                return;
//            }
//            boolean[] hasError = new boolean[1];
//            processor.applyActions(data, hasError);
//            if (!hasError[0]) {
//                WindowUtils.ALERT("Success", stageType.getDisplayName() + " data imported and applied successfully", WindowUtils.ALERT_INFORMATION);
//            } else {
//                WindowUtils.ALERT("Completed with Warnings", "Some records were skipped or failed (see previous messages)", WindowUtils.ALERT_WARNING);
//            }
//
//        } catch (IOException e) {
//            Logging.logException("ERROR", getClass().getName(), "importExcel", e);
//            WindowUtils.ALERT("Import Failed", "Failed to read Excel file.\nDetails:\n" + e.getMessage(), WindowUtils.ALERT_ERROR);
//        } catch (Exception e) {
//            Logging.logException("ERROR", getClass().getName(), "importExcel", e);
//            WindowUtils.ALERT("Error", "Unexpected error: " + e.getMessage(), WindowUtils.ALERT_ERROR);
//        }
//    }


    private void initCombo() {
        // ComboBox Init
        listStages = stageDao.getAllStages();
        stagesCombo.setItems(listStages);

        // Add listener to update machines based on selected stage
        stagesCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                listMachines = machineDao.getMachinesByStageId(newVal.getStageId());
                machinesCombo.setItems(listMachines);
            } else {
                machinesCombo.getItems().clear();
            }
        });
    }

    @FXML
    void clearSelectStageAndMachine(ActionEvent event) {
        stagesCombo.getSelectionModel().clearSelection();
        machinesCombo.getSelectionModel().clearSelection();
    }

    @FXML
    void openExportPdsPage(ActionEvent event) {
        WindowUtils.CLOSE(event);
        WindowUtils.OPEN_EXPORT_PDS_DATA();
    }

    private void initExcelIcon() {
        // Add Excel Icons To All Excel Buttons
        addExcelIcon(importExcelBtn);
        addExcelIcon(exportExcelBtn);
    }

    public void addExcelIcon(Button button) {
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/excel.png")));
        ImageView view = new ImageView(img);
        view.setFitHeight(22);
        view.setFitWidth(22);
        button.setGraphic(view);
    }

    @FXML
    void getWorkOrderData(ActionEvent event) {
        try {
            OracleIntegration subWorkOrderObj = null;
            String workOrder = workOrderTextF.getText();
            String endpointUrl = ConfigLoader.getProperty("ORACLE.URL") + workOrder;

            OracleIntegration workOrderObj = ApiCaller.callApi(endpointUrl, "GET", null);

            String subWorkOrder = workOrderObj.getSoBatch();
            if (subWorkOrder != null && !subWorkOrder.equals(workOrder)) {
                endpointUrl = ConfigLoader.getProperty("ORACLE.URL") + subWorkOrder;
                subWorkOrderObj = ApiCaller.callApi(endpointUrl, "GET", null);
            }

            workOrderTextF.setText(workOrderObj.getWo());
            stageDescriptionTextF.setText(safeText(workOrderObj.getItem_desc()));

            Platform.runLater(() -> {
                workOrderTextF.requestFocus();
                workOrderTextF.positionCaret(workOrderTextF.getText().length());
            });

        } catch (Exception ex) {
            WindowUtils.ALERT_ON_FX_THREAD("خطأ",
                    "ربما يوجد مشكلة بنظام الاوراكل برجاء الفتح والتأكد من أمر الشغل",
                    WindowUtils.ALERT_ERROR);
            Logging.logException("ERROR", this.getClass().getName(), "getWorkOrderData", ex);
        }
    }

    private String safeText(String value) {
        return value != null ? value : "";
    }

    @FXML
    void clearSearchWorkOrder(ActionEvent event) {
        clearSearchWorkOrder();
    }

    private void clearSearchWorkOrder() {
        workOrderTextF.setText("STC-2025-");
        stageDescriptionTextF.clear();
    }

    @FXML
    void refreshPage(ActionEvent event) {

    }

}
