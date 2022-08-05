package dk.sebsa.coal.events;

import dk.sebsa.coal.stoneui.RenderingSurface;
import dk.sebsa.coal.tasks.ThreadLogging;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public abstract class Layer extends RenderingSurface {
    public boolean enabled = true;

    protected void log(Object o) { ThreadLogging.log(o.toString(), this.getClass().getSimpleName()); }

    protected abstract boolean handleEvent(Event e);
    protected boolean event(Event e) {
        if(elementGroup != null && elementGroup.handleEvent(e)) return true;

        return handleEvent(e);
    }

    protected abstract void init();
    protected abstract void update();
    protected abstract void cleanup();
}
