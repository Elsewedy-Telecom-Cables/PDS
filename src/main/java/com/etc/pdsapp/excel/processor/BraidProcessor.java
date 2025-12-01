package com.etc.pdsapp.excel.processor;

import com.etc.pdsapp.dao.AppContext;
import com.etc.pdsapp.dao.BraidDao;
import com.etc.pdsapp.model.Braid;
import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.services.WindowUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.etc.pdsapp.services.WindowUtils.ALERT_ERROR;

public class BraidProcessor {

    private BraidDao braidDao = AppContext.getInstance().getBraidDao();

    public Map<Integer, String> applyActionsToDb(List<Braid> braids) {
        Map<Integer, String> results = new HashMap<>();

        for (int i = 0; i < braids.size(); i++) {
            Braid b = braids.get(i);
            String action = b.getAction() != null ? b.getAction().toUpperCase() : "";
            String resultMsg;

            try {
                switch (action) {
                    case "INSERT":
                        int generatedId = braidDao.insert(b);
                        if (generatedId > 0) {
                            resultMsg = "Inserted successfully, ID=" + generatedId;
                            Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
                                    "INSERT success for row %d: %s", i + 1, resultMsg);
                        } else {
                            resultMsg = "Failed to insert";
                            Logging.logMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb",
                                    "INSERT failed for row %d: %s", i + 1, resultMsg);
                            WindowUtils.ALERT("ERROR", "failed Insert skipped (duplicate or failed)", ALERT_ERROR);
                        }
                        break;

                    case "UPDATE":
                        boolean updated = braidDao.update(b);
                        if (updated) {
                            resultMsg = "Updated successfully";
                            Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
                                    "UPDATE success for row %d: %s", i + 1, resultMsg);
                        } else {
                            resultMsg = "Failed to update";
                            Logging.logMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb",
                                    "UPDATE failed for row %d: %s", i + 1, resultMsg);
                        }
                        break;

                    case "DELETE":
                        boolean deleted = braidDao.delete(b.getBraidId());
                        if (deleted) {
                            resultMsg = "Deleted successfully";
                            Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
                                    "DELETE success for row %d: %s", i + 1, resultMsg);
                        } else {
                            resultMsg = "Failed to delete";
                            Logging.logMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb",
                                    "DELETE failed for row %d: %s", i + 1, resultMsg);
                        }
                        break;

                    default:
                        resultMsg = "Unknown action: " + action;
                        Logging.logMessage(Logging.WARN, getClass().getName(), "applyActionsToDb",
                                "Unknown action for row %d: %s", i + 1, action);
                }
            } catch (Exception e) {
                resultMsg = "Error: " + e.getMessage();
                Logging.logExpWithMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb", e,
                        "Exception occurred for row %d", i + 1);
            }

            results.put(i + 1, resultMsg); // +1 for human rows (Excel)
        }

        return results;
    }
}