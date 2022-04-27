package dk.sebsa.coal.ecs;

public class Component {
    protected Entity entity;

    void init(Entity entity) {
        this.entity = entity;
    }
}
