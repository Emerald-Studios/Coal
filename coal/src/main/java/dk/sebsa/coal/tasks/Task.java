package dk.sebsa.coal.tasks;

/**
 * @author sebs
 * @since 1.0.0
 */
public abstract class Task {
    protected abstract String name();
    public abstract void run();
    public long startTime;
    protected TaskThread thread;

    protected void log(Object o) { ThreadLogging.log(o.toString(), this.getClass().getSimpleName()); }
    protected void warn(Object o) { ThreadLogging.warn(o.toString(), this.getClass().getSimpleName()); }
    protected void error(Object o) { ThreadLogging.error(o.toString(), this.getClass().getSimpleName()); }
}
