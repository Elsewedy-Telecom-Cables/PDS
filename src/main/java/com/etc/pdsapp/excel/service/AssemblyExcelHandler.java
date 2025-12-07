package com.etc.pdsapp.excel.service;

import com.etc.pdsapp.excel.core.StageExcelService;
import com.etc.pdsapp.excel.core.StageValidator;
import com.etc.pdsapp.excel.core.StageProcessor;
import com.etc.pdsapp.excel.util.ExcelUtils;
import com.etc.pdsapp.model.Assembly;
import com.etc.pdsapp.excel.validator.AssemblyValidator;
import com.etc.pdsapp.excel.processor.AssemblyProcessor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AssemblyExcelHandler
        implements StageExcelService<Assembly>, StageValidator<Assembly>, StageProcessor<Assembly> {

    private static final String[] HEADERS = {
            "assembly_id", "stage_description", "machine_id", "user_id",
            "line_speed", "traverse_lay", "lay_length",
            "pair_1_lay_length", "pair_2_lay_length", "pair_3_lay_length", "pair_4_lay_length",
            "pair_1_color", "pair_2_color", "pair_3_color", "pair_4_color",
            "notes", "action"
    };

    @Override
    public List<Assembly> importFromExcel(File file) throws IOException {
        List<Assembly> list = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
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

                    // Mandatory fields validation
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

                    // New decimal fields (lay lengths)
                    a.setLayLength(ExcelUtils.getCellBigDecimal(row, 6));
                    a.setPair1LayLength(ExcelUtils.getCellBigDecimal(row, 7));
                    a.setPair2LayLength(ExcelUtils.getCellBigDecimal(row, 8));
                    a.setPair3LayLength(ExcelUtils.getCellBigDecimal(row, 9));
                    a.setPair4LayLength(ExcelUtils.getCellBigDecimal(row, 10));

                    // Color fields
                    a.setPair1Color(ExcelUtils.getCellString(row, 11));
                    a.setPair2Color(ExcelUtils.getCellString(row, 12));
                    a.setPair3Color(ExcelUtils.getCellString(row, 13));
                    a.setPair4Color(ExcelUtils.getCellString(row, 14));

                    // Notes and Action
                    a.setNotes(ExcelUtils.getCellString(row, 15));
                    a.setAction(ExcelUtils.getCellString(row, 16));

                    list.add(a);

                } catch (Exception e) {
                    throw new IOException("Error reading row " + rowNum + ": " + e.getMessage(), e);
                }
            }
        }
        return list;
    }

    @Override
    public List<String> validate(List<Assembly> items) {
        return new AssemblyValidator().validate(items);
    }

    @Override
    public void applyActions(List<Assembly> items, boolean[] hasError) {
        new AssemblyProcessor().applyActionsToDb(items,hasError);
    }
}
