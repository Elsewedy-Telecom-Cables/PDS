package com.elsewedyt.pdsapp.controllers;
import com.elsewedyt.pdsapp.dao.AssemblyDAO;
import com.elsewedyt.pdsapp.dao.MachineDAO;
import com.elsewedyt.pdsapp.excel.processor.AssemblyExcelProcessor;
import com.elsewedyt.pdsapp.excel.service.AssemblyExcelService;
import com.elsewedyt.pdsapp.excel.validator.AssemblyExcelValidator;
import com.elsewedyt.pdsapp.logging.Logging;
import com.elsewedyt.pdsapp.models.Assembly;
import com.elsewedyt.pdsapp.models.Machine;
import com.elsewedyt.pdsapp.models.OracleIntegration;
import com.elsewedyt.pdsapp.models.UserContext;
import com.elsewedyt.pdsapp.services.ApiCaller;
import com.elsewedyt.pdsapp.services.ConfigLoader;
import com.elsewedyt.pdsapp.services.WindowUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddUpdatePdsDataController implements Initializable {
    @FXML private Button assemblyExportExcel_btn;
    @FXML private Button assemblyImportExcel_btn;
    @FXML private ComboBox<Machine> assemblyMachine_Comb;
    @FXML private Button braidExportExcel_btn;
    @FXML private Button braidImportExcel_btn;
    @FXML private ComboBox<Machine> braidMachine_Comb;
    @FXML private Button clear_search_work_order_btn;
    @FXML private Button openExportPds_btn;
    @FXML private Button refresh_btn;
    @FXML private AnchorPane rootPane;
    @FXML private Button search_work_order_oracle_btn;
    @FXML private TextField stage_desc_textF;
    @FXML private Label wo_lbl;
    @FXML private TextField work_order_textF;
    ObservableList<Machine> listAssemblyMachines;
    ObservableList<Machine> listBraidMachines;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> wo_lbl.requestFocus());
        work_order_textF.setText("STC-2025-");
        int userId = UserContext.getCurrentUser().getUserId() ;
         // Add Excel Icons To All Excel Buttons
        addExcelIcon(assemblyImportExcel_btn);
        addExcelIcon(assemblyExportExcel_btn);

        initCombo();
        //assembly_machine_Comb
        // Ai  هل يمكن استخدام Streams للحصول على المكن بواسطة stage id بديلا عن DAO  ام من الافضل

    }

    private void initCombo(){
        listAssemblyMachines = MachineDAO.getMachinesByStageId(2);  // Assembly
        assemblyMachine_Comb.setItems(listAssemblyMachines);
        listBraidMachines = MachineDAO.getMachinesByStageId(4);  // Braid
        braidMachine_Comb.setItems(listBraidMachines);
    }
    @FXML
    void saveAssemblyRecord(ActionEvent event) {
        try {
            int userId = UserContext.getCurrentUser().getUserId();
            Machine selectedMachine = assemblyMachine_Comb.getSelectionModel().getSelectedItem();
            String stageDesc = stage_desc_textF.getText();

            if (selectedMachine == null || stageDesc.isEmpty()) {
                WindowUtils.ALERT("Warning", "Stage and Machine are required", WindowUtils.ALERT_WARNING);
                return;
            }

            // Check duplicate
            if (AssemblyDAO.existsAssemblyRecord(stageDesc, selectedMachine.getMachineId())) {
                WindowUtils.ALERT("Error", "This combination of Stage & Machine already exists!", WindowUtils.ALERT_ERROR);
                return;
            }

            // Insert new Assembly row
            Assembly newAssembly = new Assembly();
            newAssembly.setStageDescription(stageDesc);
            newAssembly.setMachineId(selectedMachine.getMachineId());
            newAssembly.setUserId(userId);

            int  generatedId = AssemblyDAO.insert(newAssembly);
            if (generatedId != -1) {
                WindowUtils.ALERT("Success", "Assembly record saved successfully", WindowUtils.ALERT_INFORMATION);
                clearSearchWorkOrder();
                assemblyMachine_Comb.getSelectionModel().clearSelection();
            } else {
                WindowUtils.ALERT("Error", "Failed to save Assembly record", WindowUtils.ALERT_ERROR);
            }

        } catch (Exception ex) {
            Logging.logException("ERROR", this.getClass().getName(), "saveAssemblyRecord", ex);
        }
    }

    @FXML
    void assemblyExportExcel(ActionEvent event) {
        try {
            List<Assembly> assemblies = AssemblyDAO.getAll(); // DAO method

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Assemblies");

            // Header Row
            Row header = sheet.createRow(0);
            String[] columns = {"AssemblyId", "StageDescription", "LineSpeed", "MachineId", "UserId", "TraverseLay", "Notes", "Action"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Data Rows
            int rowIdx = 1;
            for (Assembly a : assemblies) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(a.getAssemblyId());
                row.createCell(1).setCellValue(a.getStageDescription());
                if (a.getLineSpeed() != null) row.createCell(2).setCellValue(a.getLineSpeed());
                row.createCell(3).setCellValue(a.getMachineId());
                if (a.getUserId() != null) row.createCell(4).setCellValue(a.getUserId());
                if (a.getTraverseLay() != null) row.createCell(5).setCellValue(a.getTraverseLay());
                row.createCell(6).setCellValue(a.getNotes() != null ? a.getNotes() : "");
                row.createCell(7).setCellValue(""); // Action يسيبها فاضية يملأها المستخدم
            }

            // Save File Dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Assembly Excel");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                    workbook.close();
                    WindowUtils.ALERT("Success", "Data exported successfully ✅", WindowUtils.ALERT_INFORMATION);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            WindowUtils.ALERT("Error", "Export failed: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }

    @FXML
    void assemblyImportExcel(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Assembly Excel");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                List<Assembly> assemblies = new ArrayList<>();

                try (FileInputStream fis = new FileInputStream(file);
                     Workbook workbook = new XSSFWorkbook(fis)) {

                    Sheet sheet = workbook.getSheetAt(0);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    Assembly a = new Assembly();
                    a.setAssemblyId(getCellInt(row, 0) != null ? getCellInt(row, 0) : 0);
                    a.setStageDescription(getCellString(row, 1));
                    a.setLineSpeed(getCellDouble(row, 2));
                    a.setMachineId(getCellInt(row, 3) != null ? getCellInt(row, 3) : 0);
                    a.setUserId(getCellInt(row, 4));
                    a.setTraverseLay(getCellDouble(row, 5));
                    a.setNotes(getCellString(row, 6));
                    a.setAction(getCellString(row, 7));

                    assemblies.add(a);
                }}

                // Validation
                AssemblyExcelValidator validator = new AssemblyExcelValidator();
                List<String> errors = validator.validate(assemblies);

                if (!errors.isEmpty()) {
                    WindowUtils.ALERT("Validation Errors", String.join("\n", errors), WindowUtils.ALERT_ERROR);
                    return;
                }

                // Apply Actions
                AssemblyExcelProcessor processor = new AssemblyExcelProcessor();
                processor.applyActionsToDb(assemblies);

                WindowUtils.ALERT("Success", "Excel data imported successfully ✅", WindowUtils.ALERT_INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            WindowUtils.ALERT("Error", "Import failed: " + e.getMessage(), WindowUtils.ALERT_ERROR);
        }
    }


    @FXML
    void braidExportExcel(ActionEvent event) {

    }
    @FXML
    void braidImportExcel(ActionEvent event) {

    }


    @FXML
    void refresh(ActionEvent event) {

    }
    public void addExcelIcon(Button button) {
        Image img = new Image(getClass().getResourceAsStream("/images/excel.png"));
        ImageView view = new ImageView(img);
        view.setFitHeight(22);
        view.setFitWidth(22);
        button.setGraphic(view);
    }
    @FXML
    void getWorkOrderData(ActionEvent event) {
        try {
            OracleIntegration subWorkOrderObj = null;
            String workOrder = work_order_textF.getText();
            String endpointUrl = ConfigLoader.getProperty("oracle.url") + workOrder;

            OracleIntegration workOrderObj = ApiCaller.callApi(endpointUrl, "GET", null);

            String subWorkOrder = workOrderObj.getSoBatch();
            if (subWorkOrder != null && !subWorkOrder.equals(workOrder)) {
                endpointUrl = ConfigLoader.getProperty("oracle.url") + subWorkOrder;
                subWorkOrderObj = ApiCaller.callApi(endpointUrl, "GET", null);
            }

            work_order_textF.setText(workOrderObj.getWo());
            stage_desc_textF.setText(safeText(workOrderObj.getItem_desc()));

        } catch (Exception ex) {
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
    private void clearSearchWorkOrder(){
        work_order_textF.clear();
        stage_desc_textF.clear();
    }
    @FXML
    void openExportPdsPage(ActionEvent event) {
        WindowUtils.CLOSE(event);
        WindowUtils.OPEN_EXPORT_PDS_DATA();
    }
    private String getCellString(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());
    }

    private Double getCellDouble(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Integer getCellInt(Row row, int colIndex) {
        Double d = getCellDouble(row, colIndex);
        return d != null ? d.intValue() : null;
    }

}
