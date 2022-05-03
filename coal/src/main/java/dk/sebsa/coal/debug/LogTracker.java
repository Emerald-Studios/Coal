package dk.sebsa.coal.debug;

import dk.sebsa.emerald.LogOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebs
 * @since 1.0.0
 */
public class LogTracker extends LogOutput {
    public record Log(String s, int level) { }
    public static final List<Log> logs = new ArrayList<>();

    @Override
    public void print(String s) {
        if(s.startsWith("WARN")) logs.add(new Log(s,1));
        else if (s.startsWith("ERR")) logs.add(new Log(s,2));
        else logs.add(new Log(s,0));
    }
}
