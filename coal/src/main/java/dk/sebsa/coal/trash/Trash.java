package dk.sebsa.coal.trash;

/**
 * @author sebs
 */
public abstract class Trash {
    public abstract void destroy();

    public Trash() {
        TrashCollector.trash.add(this);
    }
}
