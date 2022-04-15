package dk.sebsa.coal.tasks;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public abstract class Task {
    protected abstract String name();
    public abstract void run();

    public long startTime;
}
