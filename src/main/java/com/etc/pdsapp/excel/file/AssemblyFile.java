
package com.etc.pdsapp.excel.file;

import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.model.Assembly;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AssemblyFile {

    public static void exportToExcel(List<Assembly> assemblies) {
        String timestamp = new SimpleDateFormat("ddMMyyyy-HHmmss").format(new Date());
        String initialFileName = "Assembly_" + timestamp + ".xlsx";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Assembly Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName(initialFileName);
        File excelFile = fileChooser.showSaveDialog(null);

        if (excelFile == null) {
            return;
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Assembly Data");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Write headers
            String[] columns = { "Assembly ID", "Stage Description", "Machine ID", "User ID", "Line Speed", "Traverse Lay", "Notes", "Action" };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Write data
            int rowIndex = 1;
            for (Assembly a : assemblies) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(a.getAssemblyId());
                row.createCell(1).setCellValue(a.getStageDescription());
                row.createCell(2).setCellValue(a.getMachineId());
                row.createCell(3).setCellValue(a.getUserId() != null ? a.getUserId() : -1);
                row.createCell(4).setCellValue(a.getLineSpeed() != null ? a.getLineSpeed() : 0);
                row.createCell(5).setCellValue(a.getTraverseLay() != null ? a.getTraverseLay() : 0);
                row.createCell(6).setCellValue(a.getNotes() != null ? a.getNotes() : "");
                row.createCell(7).setCellValue(a.getAction() != null ? a.getAction() : "");
            }

            // Apply formatting
            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, columns.length - 1));
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream fos = new FileOutputStream(excelFile)) {
                workbook.write(fos);
            }

            // Open file
            if (Desktop.isDesktopSupported() && excelFile.exists()) {
                Desktop.getDesktop().open(excelFile);
            }

        } catch (IOException ex) {
            Logging.logException("ERROR", AssemblyFile.class.getName(), "exportToExcel", ex);
        }
    }
}