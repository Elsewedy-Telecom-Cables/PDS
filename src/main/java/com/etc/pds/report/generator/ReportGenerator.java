
package com.etc.pds.report.generator;

import com.etc.pds.model.PdsReportResponse;
import com.etc.pds.model.Stage;
import com.etc.pds.model.Machine;

import java.io.File;

public interface ReportGenerator {
    File generate(PdsReportResponse report, Object productionData, Stage stage, Machine machine) throws Exception;

    String getTemplatePath();

    default String getStageName() {
        return this.getClass().getSimpleName().replace("ReportGenerator", "");
    }
}