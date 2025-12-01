package com.etc.pdsapp.logging;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logging {
    // ===== CUSTOM LOGGERS DEFINED FROM log4j2.xml =====
    private static final Logger infoLogger = LogManager.getLogger("INFO");
    private static final Logger sqlLogger = LogManager.getLogger("SQL");
    private static final Logger errorLogger = LogManager.getLogger("ERROR");
    private static final Logger debugLogger = LogManager.getLogger("DEBUG");

    // ===== LEVEL CONSTANTS =====
    public static final String INFO = "info";
    public static final String ERROR = "error";
    public static final String WARN = "warn";
    public static final String DEBUG = "debug";

    // ===== PUBLIC LOG METHODS =====
    public static void logMessage(String level, String className, String method, String msg, Object... params) {
        writeLog(level, className, method, null, msg, params);
    }


    public static void logExpWithMessage(String level, String className, String method, Throwable ex, String msg, Object... params) {
        writeLog(level, className, method, ex, msg, params);
    }

    public static void logException(String level, String className, String method, Throwable ex) {
        writeLog(level, className, method, ex, null, null);
    }

    // ===== CORE METHOD =====
    private static void writeLog(String level, String className, String method, Throwable ex, String msg, Object... params) {

        StringBuilder MSG = new StringBuilder();
        MSG.append("#Class   : ").append(className).append("\n");
        MSG.append("#Method  : ").append(method).append("\n");

        if (msg != null) {
            MSG.append("#Message : ").append(processMessage(msg, params)).append("\n");
        }

        if (ex != null) {
            MSG.append("#EXCEPTION : ").append(ex.getMessage()).append("\n");
            if (ex.getCause() != null)
                MSG.append("#CAUSE     : ").append(ex.getCause()).append("\n");
        }

        // ===== ROUTING LOGS TO THE CORRECT LOGGER =====
        switch (level.toLowerCase()) {

            case "info":
                infoLogger.info(MSG);
                // infoLogger.info(MSG.toString(), ex);
                break;

            case "debug":
                debugLogger.debug(MSG);
                // debugLogger.debug(MSG.toString(), ex);
                break;

            case "error":
                errorLogger.error(MSG);
                //   errorLogger.error(MSG.toString(), ex);

                break;
            case "sql":
                sqlLogger.info(MSG);
                //   sqlLogger.info(MSG.toString(), ex);

                break;
            case "warn":
                infoLogger.warn(MSG);
                break;
            default:
                infoLogger.info(MSG);
                break;
        }
    }


    // ===== MESSAGE FORMATTING =====
    private static String processMessage(String msg, Object... params) {
        if (params != null && params.length > 0) {
            try {
                return String.format(msg, params);
            } catch (Exception e) {
                return msg; // fallback
            }
        }
        return msg;
    }

}
