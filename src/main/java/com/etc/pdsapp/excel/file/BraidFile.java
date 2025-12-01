
package com.etc.pdsapp.excel.file;
import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.model.Braid;
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

public class BraidFile {

    public static void exportToExcel(List<Braid> braids) {
        String timestamp = new SimpleDateFormat("ddMMyyyy-HHmmss").format(new Date());
        String initialFileName = "Braid_" + timestamp + ".xlsx";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Braid Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName(initialFileName);
        File excelFile = fileChooser.showSaveDialog(null);

        if (excelFile == null) {
            return;
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Braid Data");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] columns = { "Braid ID", "Stage Description", "Machine ID", "User ID", "Deck Speed", "Speed", "Pitch", "Notes", "Action" };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            for (Braid b : braids) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(b.getBraidId());
                row.createCell(1).setCellValue(b.getStageDescription());
                row.createCell(2).setCellValue(b.getMachineId());
                row.createCell(3).setCellValue(b.getUserId() != null ? b.getUserId() : -1);
                row.createCell(4).setCellValue(b.getDeckSpeed() != null ? b.getDeckSpeed() : 0);
                row.createCell(5).setCellValue(b.getSpeed() != null ? b.getSpeed() : 0);
                row.createCell(6).setCellValue(b.getPitch() != null ? b.getPitch() : 0);
                row.createCell(7).setCellValue(b.getNotes() != null ? b.getNotes() : "");
                row.createCell(8).setCellValue(b.getAction() != null ? b.getAction() : "");
            }

            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, columns.length - 1));
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(excelFile)) {
                workbook.write(fos);
            }

            if (Desktop.isDesktopSupported() && excelFile.exists()) {
                Desktop.getDesktop().open(excelFile);
            }

        } catch (IOException ex) {
            Logging.logException("ERROR", BraidFile.class.getName(), "exportToExcel", ex);
        }
    }
}