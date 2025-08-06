package com.twilio.oai;

import java.util.logging.Logger;


import java.util.logging.Logger;

public class LoggerUtil {
    private static final Logger LOGGER = Logger.getLogger(LoggerUtil.class.getName());

    private LoggerUtil() {
        // Private constructor to prevent instantiation
    }

    public static void logWarning(String className, String message) {
        LOGGER.warning("[" + className + "] " + message);
    }

    public static void logInfo(String className, String message) {
        LOGGER.info("[" + className + "] " + message);
    }

    public static void logSevere(String className, String message) {
        LOGGER.severe("[" + className + "] " + message);
    }
}
