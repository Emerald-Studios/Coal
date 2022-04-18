package dk.sebsa.coal.tasks;

import java.util.function.Consumer;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public abstract class Task {
    protected abstract String name();
    public abstract void run();
    public long startTime;

    protected void log(Object o) { ThreadLogging.log(o.toString(), this.getClass().getSimpleName()); }
    protected void warn(Object o) { ThreadLogging.warn(o.toString(), this.getClass().getSimpleName()); }
    protected void error(Object o) { ThreadLogging.error(o.toString(), this.getClass().getSimpleName()); }
}
