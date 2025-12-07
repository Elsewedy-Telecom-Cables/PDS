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

public class AssemblyReportGenerator implements ReportGenerator {

    @Override
    public String getTemplatePath() {
        return "/templates/AssemblyTemplate.xlsx";
    }

    @Override
    public File generate(PdsReportResponse report, Object productionData, Stage stage, Machine machine) throws Exception {
        InputStream is = getClass().getResourceAsStream(getTemplatePath());
        if (is == null) {
            throw new FileNotFoundException("Assembly template not found: " + getTemplatePath());
        }

        Assembly assemblyData = (Assembly) productionData;
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


            if (report.getSpecificationTests() != null) {
                for (SpecificationTest t : report.getSpecificationTests()) {
                    String desc = t.getTestDescription();
                    if (desc == null) continue;
                    desc = desc.trim()
                            .replace("Diamter", "Diameter")
                            .replaceAll("\\s+", " ");

                    switch (desc) {
                        case "Assembly Diameter" -> setCell.accept("B16", safeString(t.getTargetValue()));
                        case "Lay Direction"    -> setCell.accept("B18", safeString(t.getComment()));
                        case "Color Sequence"   -> setCell.accept("B19", safeString(t.getComment()));
                    }
                }
            }

            if (report.getIngredients() != null) {
                int rowIndex = 30;
                for (Ingredient ing : report.getIngredients()) {
                    setCell.accept("A" + rowIndex, ing.getCode());
                    setCell.accept("B" + rowIndex, ing.getDescription());
                    rowIndex++;
                }
            }

            if (assemblyData != null) {
                setCell.accept("B17", safeBigDecimal(assemblyData.getLayLength()));
                setCell.accept("B23", safeBigDecimal(assemblyData.getPair1LayLength()));
                setCell.accept("B24", safeBigDecimal(assemblyData.getPair2LayLength()));
                setCell.accept("B25", safeBigDecimal(assemblyData.getPair3LayLength()));
                setCell.accept("B26", safeBigDecimal(assemblyData.getPair4LayLength()));

                setCell.accept("C23", safeString(assemblyData.getPair1Color()));
                setCell.accept("C24", safeString(assemblyData.getPair2Color()));
                setCell.accept("C25", safeString(assemblyData.getPair3Color()));
                setCell.accept("C26", safeString(assemblyData.getPair4Color()));

                setCell.accept("B43", safeDouble(assemblyData.getTraverseLay()));
                setCell.accept("B46", safeDouble(assemblyData.getLineSpeed()));
                setCell.accept("B49", safeString(assemblyData.getNotes()));
            }

            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                workbook.write(out);
            }

            if (Desktop.isDesktopSupported() && outputFile.exists()) {
                Desktop.getDesktop().open(outputFile);
            }

            return outputFile;

        } finally {
            is.close();
        }
    }

    private String getExportFilePath(String workOrder) {
        return System.getProperty("user.home") + "/Desktop/PDS_Assembly_" + workOrder + "_" +
                new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()) + ".xlsx";
    }

    private String safeString(String s) { return s != null ? s.trim() : ""; }

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