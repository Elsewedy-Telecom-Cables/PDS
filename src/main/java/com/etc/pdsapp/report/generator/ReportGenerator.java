
package com.etc.pdsapp.report.generator;

import com.etc.pdsapp.model.PdsReportResponse;
import com.etc.pdsapp.model.Stage;
import com.etc.pdsapp.model.Machine;

import java.io.File;

public interface ReportGenerator {
    File generate(PdsReportResponse report, Object productionData, Stage stage, Machine machine) throws Exception;

    String getTemplatePath();

    default String getStageName() {
        return this.getClass().getSimpleName().replace("ReportGenerator", "");
    }
}