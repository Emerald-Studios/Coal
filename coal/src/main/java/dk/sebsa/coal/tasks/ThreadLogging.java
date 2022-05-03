package dk.sebsa.coal.tasks;

import dk.sebsa.Coal;
import dk.sebsa.emerald.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sebs
 * @since 1.0.0
 */
public class ThreadLogging {
    public static final List<String> toBeLogged = Collections.synchronizedList(new ArrayList<>());
    public static void log(String s, String className) { toBeLogged.add(Coal.logger.formatString(s, className, Coal.logger.getFormat())); }
    public static void warn(String s, String className) { toBeLogged.add(Coal.logger.formatString(s, className, Coal.logger.getWarnFormat())); }
    public static void error(String s, String className) { toBeLogged.add(Coal.logger.formatString(s, className, Coal.logger.getErrorFormat())); }

    public static void logAll(Logger l) {
        while(!toBeLogged.isEmpty()) {
            l.log(toBeLogged.get(0), "NoFormat");
            toBeLogged.remove(0);
        }
    }
}
