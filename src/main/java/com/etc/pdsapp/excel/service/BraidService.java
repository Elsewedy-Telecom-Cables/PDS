package com.etc.pdsapp.excel.service;

import com.etc.pdsapp.excel.util.ExcelUtils;
import com.etc.pdsapp.model.Braid;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BraidService {


    private static final String[] HEADERS = {
            "braid_id", "stage_description", "machine_id", "user_id",
            "deck_speed", "speed", "pitch", "notes", "action"
    };

    public static List<Braid> importFromExcel(File file) throws IOException {
        List<Braid> list = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next();

            int rowNum = 1;
            while (rows.hasNext()) {
                Row row = rows.next();
                rowNum++;

                try {
                    Braid b = new Braid();

                    // Avoid NullPointerExceptio
                    Integer braidId = ExcelUtils.getCellInt(row, 0);
                    String stageDesc = ExcelUtils.getCellString(row, 1);
                    Integer machineId = ExcelUtils.getCellInt(row, 2);


                    if (stageDesc == null || stageDesc.trim().isEmpty()) {
                        throw new IllegalArgumentException("Stage Description is required at row " + rowNum);
                    }
                    if (machineId == null || machineId <= 0) {
                        throw new IllegalArgumentException("Machine ID is required and must be > 0 at row " + rowNum);
                    }

                    // إذا كان braid_id فارغًا في حالة INSERT → نتركه 0 (سيُولد تلقائيًا)
                    b.setBraidId(braidId != null ? braidId : 0);
                    b.setStageDescription(stageDesc.trim());
                    b.setMachineId(machineId);

                    b.setUserId(ExcelUtils.getCellInt(row, 3));
                    b.setDeckSpeed(ExcelUtils.getCellDouble(row, 4));
                    b.setSpeed(ExcelUtils.getCellDouble(row, 5));
                    b.setPitch(ExcelUtils.getCellDouble(row, 6));
                    b.setNotes(ExcelUtils.getCellString(row, 7));
                    b.setAction(ExcelUtils.getCellString(row, 8));

                    list.add(b);

                } catch (Exception e) {
                    throw new IOException("Error reading row " + rowNum + ": " + e.getMessage(), e);
                }
            }
        }
        return list;
    }




}