package com.etc.pdsapp.excel.processor;

import com.etc.pdsapp.dao.AppContext;
import com.etc.pdsapp.dao.AssemblyDao;
import com.etc.pdsapp.model.Assembly;
import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.services.WindowUtils;
import com.etc.pdsapp.utils.StageUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.etc.pdsapp.services.WindowUtils.*;

public class AssemblyProcessor {

    private final AssemblyDao assemblyDao = AppContext.getInstance().getAssemblyDao();

    public Map<Integer, String> applyActionsToDb(List<Assembly> assemblies, boolean[] hasError) {
        Map<Integer, String> results = new HashMap<>();
        hasError[0] = false;

        for (int i = 0; i < assemblies.size(); i++) {
            Assembly a = assemblies.get(i);
            String action = a.getAction() != null ? a.getAction().toUpperCase() : "";
            String resultMsg = "Unknown error";

            try {
                switch (action) {

                    case "INSERT": {
                        String normalized = StageUtils.normalize(a.getStageDescription());

                        // Check duplicate
                        if (assemblyDao.existsAssemblyRecord(normalized, a.getMachineId())) {
                            resultMsg = "Skipped: Duplicate found for Stage+Machine";
                            WindowUtils.ALERT("Warning", resultMsg, ALERT_ERROR);

                            Logging.logMessage(Logging.WARN, getClass().getName(), "applyActionsToDb",
                                    "INSERT skipped for row %d: %s", i + 1, resultMsg);

                            results.put(i + 1, resultMsg);
                            hasError[0] = true;
                            break;
                        }

                        // Insert
                        int generatedId = assemblyDao.insert(a);
                        if (generatedId > 0) {
                            resultMsg = "Inserted successfully, ID=" + generatedId;
                            Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
                                    "INSERT success for row %d: %s", i + 1, resultMsg);
                        } else {
                            resultMsg = "Failed to insert";
                            hasError[0] = true;
                            Logging.logMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb",
                                    "INSERT failed for row %d: %s", i + 1, resultMsg);
                        }

                        results.put(i + 1, resultMsg);
                        break;
                    }

                    case "UPDATE":
                        boolean updated = assemblyDao.update(a);
                        if (updated) {
                            resultMsg = "Updated successfully";
                        } else {
                            resultMsg = "Failed to update";
                            hasError[0] = true;
                        }
                        Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
                                "UPDATE row %d: %s", i + 1, resultMsg);
                        results.put(i + 1, resultMsg);
                        break;

                    case "DELETE":
                        boolean deleted = assemblyDao.delete(a.getAssemblyId());
                        if (deleted) {
                            resultMsg = "Deleted successfully";
                        } else {
                            resultMsg = "Failed to delete";
                            hasError[0] = true;
                        }
                        Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
                                "DELETE row %d: %s", i + 1, resultMsg);
                        results.put(i + 1, resultMsg);
                        break;

                    default:
                        resultMsg = "Unknown action: " + action;
                        Logging.logMessage(Logging.WARN, getClass().getName(), "applyActionsToDb",
                                "Unknown row %d: %s", i + 1, action);
                        results.put(i + 1, resultMsg);
                        break;
                }

            } catch (Exception e) {
                resultMsg = "Error: " + e.getMessage();
                Logging.logExpWithMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb", e,
                        "Exception for row %d", i + 1);
                results.put(i + 1, resultMsg);
                hasError[0] = true;
            }
        }

        return results;
    }
}
