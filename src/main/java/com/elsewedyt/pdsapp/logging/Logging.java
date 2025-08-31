package com.elsewedyt.pdsapp.logging;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logging {
    // Logger instance for this class
    // Define loggers
    private static final Logger logger = LogManager.getLogger(Logging.class);
    private static final Logger sqlLogger = LogManager.getLogger("SQL");
    private static final Logger errorLogger = LogManager.getLogger("errors");

    // Define logger type
    public static String INFO = "info";
    public static String ERROR = "error";
    public static String WARN = "warn";
    public static String DEBUG = "debug";


    public static void logMessage(String level,String className,String method,String msg, Object... params) {
        writeLog(level,className,method,null,msg,params);
    }
    public static void logExpWithMessage(String level,String className,String method,Throwable ex ,String msg, Object... params) {
        writeLog(level,className,method,ex,msg,params);
    }
    public static void logException(String level,String className,String method,Throwable ex) {
        writeLog(level,className,method,ex,null,null);
    }
    public static void writeLog(String level,String className,String method,Throwable ex ,String msg, Object... params){
        StringBuilder MSG = new StringBuilder();
        MSG.append("#Class   : "+className+"\n");
        MSG.append("#Method  : "+method+"\n");
        if (msg!=null && params != null){
            MSG.append("#Message : "+processMessage(msg,params)+"\n");
        }
        if (ex != null){
            MSG.append("#EXCEPTION : "+ex.getMessage()+"\n");
            MSG.append("#EXCEPTION : "+ex.getCause()+"\n");
        }
        switch (level.toLowerCase()) {
            case "info":
                logger.info(MSG);
                break;
            case "debug":
                sqlLogger.debug(MSG);
                break;
            case "error":
                errorLogger.error(MSG);
                break;
            case "warn":
                logger.warn(MSG);
                break;
            default:
                logger.info(MSG);
                break;
        }
    }

    private static String processMessage(String msg, Object... params) {
        if (params != null && params.length > 0) {
                return String.format(msg, params);
        }
        return msg;
    }

}
