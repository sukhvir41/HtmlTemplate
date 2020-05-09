package org.ht.cli;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogManager {

    private final static Logger LOGGER = Logger.getAnonymousLogger();


    public static void setLoggerLevel(Level level) {
        LOGGER.setLevel(level);
    }

    public static Logger getLogger() {
        return LOGGER;
    }


}
