
package com.etc.pdsapp.excel.service;
import com.etc.pdsapp.excel.util.ExcelUtils;
import com.etc.pdsapp.model.Assembly;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AssemblyService {

    private static final String[] HEADERS = {
            "assembly_id", "stage_description", "machine_id", "user_id",
            "line_speed", "traverse_lay", "notes", "action"
    };

    public static List<Assembly> importFromExcel(File file) throws IOException {
        List<Assembly> list = new ArrayList<>();

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
                    Assembly a = new Assembly();

                    Integer assemblyId = ExcelUtils.getCellInt(row, 0);
                    String stageDesc = ExcelUtils.getCellString(row, 1);
                    Integer machineId = ExcelUtils.getCellInt(row, 2);

                    if (stageDesc == null || stageDesc.trim().isEmpty()) {
                        throw new IllegalArgumentException("Stage Description is required at row " + rowNum);
                    }
                    if (machineId == null || machineId <= 0) {
                        throw new IllegalArgumentException("Machine ID is required and must be > 0 at row " + rowNum);
                    }

                    a.setAssemblyId(assemblyId != null ? assemblyId : 0);
                    a.setStageDescription(stageDesc.trim());
                    a.setMachineId(machineId);

                    a.setUserId(ExcelUtils.getCellInt(row, 3));
                    a.setLineSpeed(ExcelUtils.getCellDouble(row, 4));
                    a.setTraverseLay(ExcelUtils.getCellDouble(row, 5));
                    a.setNotes(ExcelUtils.getCellString(row, 6));
                    a.setAction(ExcelUtils.getCellString(row, 7));

                    list.add(a);

                } catch (Exception e) {
                    throw new IOException("Error reading row " + rowNum + ": " + e.getMessage(), e);
                }
            }
        }
        return list;
    }
}
