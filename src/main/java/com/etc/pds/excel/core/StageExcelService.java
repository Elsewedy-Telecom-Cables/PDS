package com.etc.pds.excel.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface StageExcelService<T> {
    List<T> importFromExcel(File file) throws IOException;
}


