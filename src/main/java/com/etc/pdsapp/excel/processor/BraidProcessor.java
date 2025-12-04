package com.etc.pdsapp.excel.processor;

import com.etc.pdsapp.dao.AppContext;
import com.etc.pdsapp.dao.BraidDao;
import com.etc.pdsapp.model.Braid;
import com.etc.pdsapp.logging.Logging;
import com.etc.pdsapp.services.WindowUtils;
import com.etc.pdsapp.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.etc.pdsapp.services.WindowUtils.ALERT_ERROR;

public class BraidProcessor {

    private final BraidDao braidDao = AppContext.getInstance().getBraidDao();


    public Map<Integer, String> applyActionsToDb(List<Braid> braids) {
        Map<Integer, String> results = new HashMap<>();


        for (int i = 0; i < braids.size(); i++) {
            Braid b = braids.get(i);
            String action = b.getAction() != null ? b.getAction().toUpperCase() : "";
            String resultMsg = "Unknown error";

            try {
                switch (action) {

                    case "INSERT": {
                        String normalized = StageUtils.normalize(b.getStageDescription());

                        //  check duplicate
                        if (braidDao.existsBraidRecord(normalized, b.getMachineId())) {
                            resultMsg = "Skipped: Duplicate found for Stage+Machine";
                            WindowUtils.ALERT("Warning", resultMsg, ALERT_ERROR);

                            Logging.logMessage(Logging.WARN, getClass().getName(), "applyActionsToDb",
                                    "INSERT skipped for row %d: %s", i + 1, resultMsg);

                            results.put(i + 1, resultMsg);  //  سجل النتيجة هنا فقط
                            break;
                        }

                        // ➕ Insert
                        int generatedId = braidDao.insertBraid(b);
                        if (generatedId > 0) {
                            resultMsg = "Inserted successfully, ID=" + generatedId;

                            Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
                                    "INSERT success for row %d: %s", i + 1, resultMsg);
                        } else {
                            resultMsg = "Failed to insert";

                            Logging.logMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb",
                                    "INSERT failed for row %d: %s", i + 1, resultMsg);
                        }

                        results.put(i + 1, resultMsg);
                        break;
                    }

                    case "UPDATE":
                        boolean updated = braidDao.updateBraid(b);
                        if (updated) {
                            resultMsg = "Updated successfully";
                        } else {
                            resultMsg = "Failed to update";
                        }

                        Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
                                "UPDATE row %d: %s", i + 1, resultMsg);

                        results.put(i + 1, resultMsg);
                        break;

                    case "DELETE":
                        boolean deleted = braidDao.deleteBraid(b.getBraidId());
                        if (deleted) {
                            resultMsg = "Deleted successfully";
                        } else {
                            resultMsg = "Failed to delete";
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
                }

            } catch (Exception e) {
                resultMsg = "Error: " + e.getMessage();

                Logging.logExpWithMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb", e,
                        "Exception for row %d", i + 1);

                results.put(i + 1, resultMsg);
            }
        }

        return results;
    }




//    public Map<Integer, String> applyActionsToDb(List<Braid> braids) {
//        Map<Integer, String> results = new HashMap<>();
//
//        for (int i = 0; i < braids.size(); i++) {
//            Braid b = braids.get(i);
//            String action = b.getAction() != null ? b.getAction().toUpperCase() : "";
//            String resultMsg;
//
//            try {
//                switch (action) {
//                    case "INSERT": {
//                        String normalized = StageUtils.normalize(b.getStageDescription());
//
//                        // التحقق من التكرار قبل الإدخال
//                        if (braidDao.existsBraidRecord(normalized, b.getMachineId())) {
//                            resultMsg = "Skipped: Duplicate found for Stage+Machine";
//                            WindowUtils.ALERT("Warning", resultMsg, ALERT_ERROR);
//                            Logging.logMessage(Logging.WARN, getClass().getName(), "applyActionsToDb",
//                                    "INSERT skipped for row %d: %s", i + 1, resultMsg);
//                            results.put(i + 1, resultMsg);  //  تسجيل سبب التوقف
//                            break; //  يمنع استكمال باقي كود INSERT دون الخروج من الدالة
//                        } else {
//                            int generatedId = braidDao.insertBraid(b);
//                            if (generatedId > 0) {
//                                resultMsg = "Inserted successfully, ID=" + generatedId;
//                                Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
//                                        "INSERT success for row %d: %s", i + 1, resultMsg);
//                            } else {
//                                resultMsg = "Failed to insert";
//                                Logging.logMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb",
//                                        "INSERT failed for row %d: %s", i + 1, resultMsg);
//                            }
//                        }
//                        break;
//                    }
//
//
////                switch (action) {
////                    // التحقق من التكرار قبل الإدخال
////                    if (braidDao.existsBraidRecord(normalize(b.getStageDescription()), b.getMachineId())) {
////                        resultMsg = "Skipped: Duplicate found for Stage+Machine";
////                        Logging.logMessage(Logging.WARN, getClass().getName(), "applyActionsToDb",
////                                "INSERT skipped for row %d: %s", i + 1, resultMsg);
////                    } else {
////                        int generatedId = braidDao.insertBraid(b);
////                        if (generatedId > 0) {
////                            resultMsg = "Inserted successfully, ID=" + generatedId;
////                            Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
////                                    "INSERT success for row %d: %s", i + 1, resultMsg);
////                        } else {
////                            resultMsg = "Failed to insert";
////                            Logging.logMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb",
////                                    "INSERT failed for row %d: %s", i + 1, resultMsg);
////                        }
////                    }
////                    break;
//
//
//                    case "UPDATE":
//                        boolean updated = braidDao.updateBraid(b);
//                        if (updated) {
//                            resultMsg = "Updated successfully";
//                            Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
//                                    "UPDATE success for row %d: %s", i + 1, resultMsg);
//                        } else {
//                            resultMsg = "Failed to update";
//                            Logging.logMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb",
//                                    "UPDATE failed for row %d: %s", i + 1, resultMsg);
//                        }
//                        break;
//
//                    case "DELETE":
//                        boolean deleted = braidDao.deleteBraid(b.getBraidId());
//                        if (deleted) {
//                            resultMsg = "Deleted successfully";
//                            Logging.logMessage(Logging.INFO, getClass().getName(), "applyActionsToDb",
//                                    "DELETE success for row %d: %s", i + 1, resultMsg);
//                        } else {
//                            resultMsg = "Failed to delete";
//                            Logging.logMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb",
//                                    "DELETE failed for row %d: %s", i + 1, resultMsg);
//                        }
//                        break;
//
//                    default:
//                        resultMsg = "Unknown action: " + action;
//                        Logging.logMessage(Logging.WARN, getClass().getName(), "applyActionsToDb",
//                                "Unknown action for row %d: %s", i + 1, action);
//                }
//            } catch (Exception e) {
//                resultMsg = "Error: " + e.getMessage();
//                Logging.logExpWithMessage(Logging.ERROR, getClass().getName(), "applyActionsToDb", e,
//                        "Exception occurred for row %d", i + 1);
//            }
//
//            results.put(i + 1, resultMsg); // +1 for human rows (Excel)
//        }
//
//
//        return results;
//    }
}