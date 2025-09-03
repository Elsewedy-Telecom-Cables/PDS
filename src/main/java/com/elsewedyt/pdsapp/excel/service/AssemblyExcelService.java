package com.elsewedyt.pdsapp.excel.service;

import com.elsewedyt.pdsapp.models.Assembly;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AssemblyExcelService {

    private static final String[] HEADERS = {
            "assembly_id", "stage_description", "machine_id", "user_id",
            "line_speed", "traverse_lay", "notes", "action"
    };

    // Export assemblies to Excel
    public static void exportToExcel(List<Assembly> assemblies, File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Assembly");

            // header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }

            // data rows
            int rowIdx = 1;
            for (Assembly a : assemblies) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(a.getAssemblyId());
                row.createCell(1).setCellValue(a.getStageDescription());
                row.createCell(2).setCellValue(a.getMachineId());
                if (a.getUserId() != null) row.createCell(3).setCellValue(a.getUserId());
                if (a.getLineSpeed() != null) row.createCell(4).setCellValue(a.getLineSpeed());
                if (a.getTraverseLay() != null) row.createCell(5).setCellValue(a.getTraverseLay());
                if (a.getNotes() != null) row.createCell(6).setCellValue(a.getNotes());

                // عمود action نخليه فاضي افتراضيًا
                row.createCell(7).setCellValue(a.getAction() != null ? a.getAction() : "");
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }

    // Import assemblies from Excel
    public static List<Assembly> importFromExcel(File file) throws IOException {
        List<Assembly> list = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // skip header

            while (rows.hasNext()) {
                Row row = rows.next();
                Assembly a = new Assembly();

                a.setAssemblyId((int) row.getCell(0).getNumericCellValue());
                a.setStageDescription(row.getCell(1).getStringCellValue());
                a.setMachineId((int) row.getCell(2).getNumericCellValue());

                if (row.getCell(3) != null)
                    a.setUserId((int) row.getCell(3).getNumericCellValue());

                if (row.getCell(4) != null)
                    a.setLineSpeed(row.getCell(4).getNumericCellValue());

                if (row.getCell(5) != null)
                    a.setTraverseLay(row.getCell(5).getNumericCellValue());

                if (row.getCell(6) != null)
                    a.setNotes(row.getCell(6).getStringCellValue());

                if (row.getCell(7) != null)
                    a.setAction(row.getCell(7).getStringCellValue());

                list.add(a);
            }
        }
        return list;
    }
}
