package com.etc.pdsapp.report.generator;

import com.etc.pdsapp.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Desktop;

public class BraidReportGenerator implements ReportGenerator {

    @Override
    public String getTemplatePath() {
        return "/templates/BraidTemplate.xlsx";
    }

    @Override
    public File generate(PdsReportResponse report, Object productionData, Stage stage, Machine machine) throws Exception {
        InputStream is = getClass().getResourceAsStream(getTemplatePath());
        if (is == null) {
            throw new FileNotFoundException("Braid template not found: " + getTemplatePath());
        }

        Braid braidData = (Braid) productionData;
        File outputFile = new File(getExportFilePath(report.getWorkOrder()));

        try (XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            java.util.function.BiConsumer<String, Object> setCell = (address, value) -> {
                CellReference ref = new CellReference(address);
                Row row = sheet.getRow(ref.getRow());
                if (row == null) row = sheet.createRow(ref.getRow());
                Cell cell = row.getCell(ref.getCol(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                if (value instanceof String s) {
                    cell.setCellValue(s);
                } else if (value instanceof Number n) {
                    cell.setCellValue(n.doubleValue());
                } else if (value != null) {
                    cell.setCellValue(value.toString());
                } else {
                    cell.setBlank();
                }
            };

            // Header Information
            setCell.accept("B9",  report.getWorkOrder());
            setCell.accept("D9",  new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            setCell.accept("B10", report.getSalesOrder());
            setCell.accept("D10", report.getSoItemCode());
            setCell.accept("B11", report.getTds());
            setCell.accept("D11", report.getProducedItemDescription());
            setCell.accept("B12", report.getCustomerName());
            setCell.accept("D12", report.getItemSize());
            setCell.accept("B13", stage.getStageName());
            setCell.accept("D13", machine.getMachineName());

            // Specification Tests
            if (report.getSpecificationTests() != null) {
                for (SpecificationTest t : report.getSpecificationTests()) {
                    String rawDesc = t.getTestDescription();
                    if (rawDesc == null) continue;

                    String desc = rawDesc.trim()
                            .replace("Diamter", "Diameter")
                            .replaceAll("\\s+", " ");

                    switch (desc) {
                        case "Braid Construction" ->
                                setCell.accept("B16", safeString(t.getComment()));

                        case "Braid Coverage" ->
                                setCell.accept("D16", safeString(t.getTargetValue()));

                        case "Diameter After Braiding" ->
                                setCell.accept("B17", safeString(t.getTargetValue()));

                        case "Lay Length" ->
                                setCell.accept("D17", safeString(t.getTargetValue()));

                        case "Braid Angle" ->
                                setCell.accept("B18", safeString(t.getTargetValue()));

                        case "Min. ALPET Overlap" ->
                                setCell.accept("D18", safeString(t.getTargetValue()));
                    }
                }
            }

            // Ingredients Table
            if (report.getIngredients() != null) {
                int rowIndex = 22; // Row 23 in Excel (0-based index = 22)
                for (Ingredient ing : report.getIngredients()) {
                    setCell.accept("A" + rowIndex, ing.getCode());
                    setCell.accept("B" + rowIndex, ing.getDescription());
                    rowIndex++;
                }
            }

            // Production Data from Database
            if (braidData != null) {
                setCell.accept("B27", safeDouble(braidData.getSpeed()));
                setCell.accept("B28", safeDouble(braidData.getDeckSpeed()));
                setCell.accept("C31", safeDouble(braidData.getPitch()));
                setCell.accept("B34", safeString(braidData.getNotes()));
            } else {
                System.out.println("No production data found for Braid stage and machine!");
            }

            // Save file
            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                workbook.write(out);
            }

            // Open automatically
            if (Desktop.isDesktopSupported() && outputFile.exists()) {
                Desktop.getDesktop().open(outputFile);
            }

            return outputFile;

        } finally {
            is.close();
        }
    }

    private String getExportFilePath(String workOrder) {
        return System.getProperty("user.home") + "/Desktop/PDS_Braid_" + workOrder + "_" +
                new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()) + ".xlsx";
    }

    private String safeString(String s) {
        return s != null ? s.trim() : "";

    }

    private String safeBigDecimal(BigDecimal value) {
        return value != null
                ? value.stripTrailingZeros().toPlainString()
                : "";
    }
    private String safeDouble(Double d) {
        return d != null
                ? BigDecimal.valueOf(d).stripTrailingZeros().toPlainString()
                : "";
    }
}
