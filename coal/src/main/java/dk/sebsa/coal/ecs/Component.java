package dk.sebsa.coal.ecs;

/**
 * @author sebs
 * @since 1.0.0
 */
public abstract class Component {
    protected Entity entity;

    protected abstract void load();
    protected abstract void update();
    protected abstract void lateUpdate();

    void init(Entity entity) {
        this.entity = entity;
    }
}
