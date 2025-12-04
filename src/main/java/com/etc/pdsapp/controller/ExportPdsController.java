package com.etc.pdsapp.controller;
import com.etc.pdsapp.dao.*;
import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.model.Braid;
import com.etc.pdsapp.model.Machine;
import com.etc.pdsapp.model.Stage;
import com.etc.pdsapp.model.UserContext;
import com.etc.pdsapp.utils.StageUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.etc.pdsapp.model.PdsReportResponse;
import com.etc.pdsapp.services.ApiCaller;
import com.etc.pdsapp.services.WindowUtils;
import javafx.concurrent.Task;
import com.etc.pdsapp.model.*;
import javafx.stage.Modality;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.concurrent.Task;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExportPdsController implements Initializable {

    @FXML private Label date_lbl;
    @FXML private ImageView logo_ImageView;
    @FXML private Label welcome_lbl;
    @FXML private Label woLabel;
    @FXML private TextField workOrderTextF;
    @FXML private TextField stageDescriptionTextF;


    @FXML private Button exportExcelBtn;
    @FXML private ComboBox<Machine> machinesCombo;
    @FXML private ComboBox<Stage> stagesCombo;

    // Lists
    ObservableList<Stage> listStages;
    ObservableList<Machine> listMachines;

    private PdsReportResponse currentReport;


    private final AssemblyDao assemblyDao = AppContext.getInstance().getAssemblyDao();
    private final BraidDao braidDao = AppContext.getInstance().getBraidDao();
    private final MachineDao machineDao = AppContext.getInstance().getMachineDao();
    private final StageDao stageDao = AppContext.getInstance().getStageDao();
    private final UserDao userDao = AppContext.getInstance().getUserDao();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // Platform.runLater(() -> woLabel.requestFocus());
        workOrderTextF.setText("STC-2025-");
        Platform.runLater(() -> {
            workOrderTextF.requestFocus();
            workOrderTextF.positionCaret(workOrderTextF.getText().length());
        });
        Image img = new Image(Objects.requireNonNull(MainController.class.getResourceAsStream("/images/etc_logo.png")));
        logo_ImageView.setImage(img);
        String msg = ("Welcome : " + UserContext.getCurrentUser().getFullName());
        welcome_lbl.setText(msg);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy  hh:mm a");
        date_lbl.setText(dateFormat2.format(date) + " ");
        initCombo();

    }


    public void showBlockingInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // منع إغلاق النافذة إلا بزر OK
        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
        alert.getDialogPane().setDisable(false);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait(); // Blocking
    }

    private Alert loadingAlert;
    private Button okBtn;
    public void showLoadingAlert(String message) {
        loadingAlert = new Alert(Alert.AlertType.INFORMATION);
        loadingAlert.setTitle("Loading");
        loadingAlert.setHeaderText(null);
        loadingAlert.setContentText(message);

        okBtn = (Button) loadingAlert.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setDisable(true); // منع المستخدم من الضغط
        loadingAlert.initModality(Modality.APPLICATION_MODAL);

        loadingAlert.show();
    }

    // call this after data is loaded
    public void enableAlertOk() {
        if (okBtn != null) okBtn.setDisable(false);
    }


    public boolean showConfirmNoMatch() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("No Production Match Found");
        alert.setHeaderText("No matching production data found!");
        alert.setContentText("Do you want to continue and export the report using Oracle data only?");

        ButtonType yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noBtn  = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesBtn, noBtn);

        // Blocking
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesBtn;
    }



    @FXML
    void getPdsData(ActionEvent event) {
        String workOrderInput = workOrderTextF.getText().trim();

        if (workOrderInput.isEmpty() || workOrderInput.equals("STC-2025-")) {
            WindowUtils.ALERT("Warning", "Please enter a valid Work Order number", WindowUtils.ALERT_WARNING);
            return;
        }

        stageDescriptionTextF.setText("Please Wait 45 Seconds As Minimum - Loading data from Oracle...");
        //
       //  WindowUtils.ALERT("انتظر", "حتى يتم جلب البيانات من نظام الاوراكل \n مدة الانتظار لا تقل عن 45 ثانية", WindowUtils.ALERT_INFORMATION);

        showLoadingAlert("Please wait... fetching data from Oracle");

        Task<PdsReportResponse> task = new Task<>() {
            @Override
            protected PdsReportResponse call() {
                return ApiCaller.fetchPdsReport(workOrderInput);
            }

            @Override
            protected void succeeded() {
                currentReport = getValue();

                Platform.runLater(() -> {
                    if (currentReport == null || currentReport.getWorkOrder() == null) {
                        stageDescriptionTextF.setText("");
                        WindowUtils.ALERT("Not Found", "No data found for Work Order: " + workOrderInput, WindowUtils.ALERT_ERROR);
                        return;
                    }

                    stageDescriptionTextF.setText(currentReport.getProducedItemDescription());
                  //  showBlockingInfo("Success", "PDS Data Loaded.\nYou may now export the report.");

                    enableAlertOk(); // السماح بالضغط على OK بعد وصول البيانات

                 //   WindowUtils.ALERT("Success", "PDS Data loaded successfully!\nYou can now export the report.", WindowUtils.ALERT_INFORMATION);

                 //   printApiOnConsole("Report"+ currentReport);

                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    currentReport = null;
                    stageDescriptionTextF.setText("");
                    Throwable ex = getException();
                    Logging.logException("ERROR", ExportPdsController.class.getName(), "getPdsData", ex);
                    WindowUtils.ALERT("API Error", "Failed to connect to Oracle.\nCheck internet or server.", WindowUtils.ALERT_ERROR);
                });
            }
        };

        new Thread(task).start();
    }

    @FXML
    void exportPdsExcel(ActionEvent event) {

        if (currentReport == null) {
            WindowUtils.ALERT("No Data", "Please load PDS data first using 'Get PDS Data'", WindowUtils.ALERT_WARNING);
            return;
        }

        Stage selectedStage = stagesCombo.getSelectionModel().getSelectedItem();
        Machine selectedMachine = machinesCombo.getSelectionModel().getSelectedItem();
        if (selectedStage == null || selectedMachine == null) {
            WindowUtils.ALERT("Selection Required", "Please select Stage and Machine", WindowUtils.ALERT_WARNING);
            return;
        }

        // منع أي تفاعل أثناء التصدير
        exportExcelBtn.setDisable(true);
        exportExcelBtn.setText("Generating Report... Please wait");

        Task<File> exportTask = new Task<>() {
            @Override
            protected File call() throws Exception {

                updateMessage("Fetching production data...");

                // Normalize API description BEFORE sending to DAO
                String rawDesc = currentReport.getProducedItemDescription();
                String normalizedApi = StageUtils.normalize(rawDesc);

//                System.out.println("Original API Desc  : [" + rawDesc + "]");
//                System.out.println("Normalized API Desc: [" + normalizedApi + "]");
//                System.out.println("Machine ID: " + selectedMachine.getMachineId());

                Braid productionData = braidDao.getByStageDescriptionAndMachine(
                        normalizedApi,
                        selectedMachine.getMachineId()
                );

                if (productionData == null) {

                    final boolean[] continueFlag = {false};

                    Platform.runLater(() -> {
                        continueFlag[0] = showConfirmNoMatch();
                    });

                    // انتظر JavaFX Thread يرجع القرار
                    while (!continueFlag[0]) {
                        Thread.sleep(50);
                    }

                    if (!continueFlag[0]) {
                        return null; // stop export
                    }
                }



                updateMessage("Creating Excel file...");
                File file = generateBraidExcelReportProperly(currentReport, productionData, selectedStage, selectedMachine);

                updateMessage("Done!");
                return file;
            }


            @Override
            protected void succeeded() {
                File file = getValue();
                Platform.runLater(() -> {
                    exportExcelBtn.setText("Export PDS Report");
                    exportExcelBtn.setDisable(false);

                    WindowUtils.ALERT("Success", "Report exported successfully!\nOpening file...", WindowUtils.ALERT_INFORMATION);

                    // Open file automatically
                    if (Desktop.isDesktopSupported()) {
                        new Thread(() -> {
                            try {
                                Desktop.getDesktop().open(file);
                            } catch (IOException e) {
                                Logging.logException("ERROR", getClass().getName(), "openExcel", e);
                            }
                        }).start();
                    }
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    exportExcelBtn.setText("Export PDS Report");
                    exportExcelBtn.setDisable(false);
                    Throwable ex = getException();
                    Logging.logException("ERROR", ExportPdsController.class.getName(), "exportPdsExcel", ex);
                    WindowUtils.ALERT("Export Failed", ex.getMessage(), WindowUtils.ALERT_ERROR);
                });
            }

            @Override
            protected void updateMessage(String message) {
                Platform.runLater(() -> exportExcelBtn.setText(message));
            }
        };

        new Thread(exportTask).start();
    }


    private File generateBraidExcelReportProperly(PdsReportResponse report, Braid braidData,
                                                  Stage stage, Machine machine) throws Exception {

        String templatePath = "/templates/BraidTemplate.xlsx";
        InputStream is = getClass().getResourceAsStream(templatePath);
        if (is == null) throw new FileNotFoundException("Template not found!");

        File outputFile = new File(getExportFilePath(report.getWorkOrder()));
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);

        java.util.function.BiConsumer<String, Object> setCell = (address, value) -> {
            CellReference ref = new CellReference(address);
            Row row = sheet.getRow(ref.getRow());
            if (row == null) row = sheet.createRow(ref.getRow());
            Cell cell = row.getCell(ref.getCol(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (value instanceof String) cell.setCellValue((String) value);
            else if (value instanceof Number) cell.setCellValue(((Number) value).doubleValue());
            else if (value != null) cell.setCellValue(value.toString());
            else cell.setBlank();
        };

        setCell.accept("B9", report.getWorkOrder()); // WO
        setCell.accept("D9", new SimpleDateFormat("dd-MM-yyyy").format(new Date())); // Date
        setCell.accept("B10", report.getSalesOrder()); // Sales Order No
        setCell.accept("D10", report.getSoItemCode()); // Product Code
        setCell.accept("B11", report.getTds()); // T.D.S NO.
        setCell.accept("D11", report.getProducedItemDescription()); // Product Description
        setCell.accept("B12", report.getCustomerName()); // Customer
        setCell.accept("D12", report.getItemSize()); // Size
        setCell.accept("B13", stage.getStageName()); // Stage
        setCell.accept("D13", machine.getMachineName()); // Braiding Machine


        if (report.getSpecificationTests() != null) {
            for (SpecificationTest t : report.getSpecificationTests()) {
                String rawDesc = t.getTestDescription();
                String desc = rawDesc != null ? rawDesc.trim() : "";

                desc = desc.replace("Diamter", "Diameter")
                        .replace("Braiding", "Braiding")
                        .replace("  ", " "); // إزالة المسافات المكررة

                switch (desc) {
                    case "Braid Construction" ->
                            setCell.accept("B16", t.getComment() != null ? t.getComment().trim() : "");

                    case "Braid Coverage" ->
                            setCell.accept("D16", t.getTargetValue());

                    case "Diameter After Braiding" -> {
                        setCell.accept("B17", t.getTargetValue());
                    }

                    case "Lay Length" ->
                            setCell.accept("D17", t.getTargetValue());

                    case "Braid Angle" ->
                            setCell.accept("B18", t.getTargetValue());

                    case "Min. ALPET Overlap" ->
                            setCell.accept("D18", t.getTargetValue());
                }
            }
        }



        if (report.getIngredients() != null) {
            int rowIndex = 22; // Start from row 23 (0-based 22)
            for (Ingredient ing : report.getIngredients()) {
                setCell.accept("A" + rowIndex, ing.getCode());
                setCell.accept("B" + rowIndex, ing.getDescription());
                rowIndex++;
            }
        }

        if (braidData != null) {
            setCell.accept("B27", safeDouble(braidData.getSpeed())); // Speed
            setCell.accept("B28", safeDouble(braidData.getDeckSpeed())); // Deck Speed
            setCell.accept("C31", safeDouble(braidData.getPitch())); // Pitch
            setCell.accept("B34", braidData.getNotes() != null ? braidData.getNotes() : ""); // Notes
        }
        else {
            System.out.println("No production data found for this stage and machine!");
        }

        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            workbook.write(out);
        }

        workbook.close();
        is.close();

        // Open file automatically
        if (Desktop.isDesktopSupported() && outputFile.exists()) {
            Desktop.getDesktop().open(outputFile);
        }

        return outputFile;
    }


    // Safe double to string
    private String safeDouble(Double d) {
        return d != null ? String.format("%.2f", d) : "";
    }

    // Generate export path
    private String getExportFilePath(String wo) {
        String timestamp = new SimpleDateFormat("ddMMyyyy-HHmm").format(new Date());
        String fileName = "PDS_Report_" + wo + "_" + timestamp + ".xlsx";
        return System.getProperty("user.home") + "/Desktop/" + fileName;
    }



    @FXML
    void clearSelectStageAndMachine(ActionEvent event) {
        stagesCombo.getSelectionModel().clearSelection();
        machinesCombo.getSelectionModel().clearSelection();
    }

    @FXML
    void clearPdsData(ActionEvent event) {
            clearPdsDataHelp();
       }

    private void clearPdsDataHelp() {
        workOrderTextF.setText("STC-2025-");
        stageDescriptionTextF.clear();
      }

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



    void printApiOnConsole(String apiResponse){
        System.out.printf("%s%n", apiResponse);
    }


}
