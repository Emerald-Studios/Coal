package dk.sebsa.coal.events;

import dk.sebsa.coal.tasks.ThreadLogging;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public abstract class Layer {
    public boolean enabled = true;

    protected void log(Object o) { ThreadLogging.log(o.toString()); }

    protected abstract boolean handleEvent(Event e);
    protected abstract void init();
    protected abstract void update();
    protected abstract void render();
    protected abstract void cleanup();
}
