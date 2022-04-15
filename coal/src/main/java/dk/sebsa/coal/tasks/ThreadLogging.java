package dk.sebsa.coal.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.sebsa.Coal;
import dk.sebsa.emerald.Logger;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class ThreadLogging {
    public static  List<String> toBeLogged = Collections.synchronizedList(new ArrayList<String>());
    public static void log(String s) { toBeLogged.add(Coal.logger.formatString(s, "TaskThread")); }
    public static void logAll(Logger l) {
        while(!toBeLogged.isEmpty()) {
            l.log(toBeLogged.get(0), "NoFormat");
            toBeLogged.remove(0);
        }
    }
}
