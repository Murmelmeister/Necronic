package de.murmelmeister.lobby.util;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class ConfigUtil {
    /**
     * Checks if the path and the file are created correctly.
     *
     * @param logger   Outputs in the console
     * @param file     The file that will be checked
     * @param fileName The name of the file
     */
    public static void createFile(Logger logger, File file, String fileName) {
        if (!(file.getParentFile().exists())) {
            boolean b = file.getParentFile().mkdirs();
            if (!(b)) logger.warn("The plugin can not create a second folder.");
        }
        if (!(file.exists())) {
            try {
                boolean b = file.createNewFile();
                if (!(b)) logger.warn(String.format("The plugin can not create the file '%s'.", fileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
