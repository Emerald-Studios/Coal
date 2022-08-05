package dk.sebsa.coal.stoneui;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sebs
 */
public class State {
    private final Map<String, Object> states = new HashMap<>();

    public void set(String key, Object o) {
        states.put(key, o);
    }

    public Object get(String key) {
        return states.get(key);
    }
    public boolean exists(String key) { return states.containsKey(key); }
}
