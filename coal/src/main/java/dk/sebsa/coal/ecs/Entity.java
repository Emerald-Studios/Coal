package dk.sebsa.coal.ecs;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author sebs
 * @since 1.0.0
 */
public class Entity {
    @Getter
    private final List<Entity> children;
    @Getter
    private List<Component> components;
    @Getter
    private Entity parent;
    public final Transform transform;
    public static final Entity master = new Entity(0);
    @Getter
    public String name = "New Entity";
    @Getter
    private final String id = UUID.randomUUID().toString();

    private Entity(int i) { children = new ArrayList<>(); transform = new MasterEntityTransform(this); name = "COAL-INTERNAL-MASTER"; }

    public Entity() {
        this.parent = master;
        children = new ArrayList<>();
        components = new ArrayList<>();
        transform = new Transform(this);
    }

    public Entity(Entity parent) {
        this.parent = parent;
        children = new ArrayList<>();
        components = new ArrayList<>();
        transform = new Transform(this);
    }

    // Parent And Child Stuff
    public void parent(Entity e) {
        if(e == null) e = master;
        if(parent != null) {
            if(parent != e) parent.removeChild(this);
            else return;
        }
        parent = e;
        parent.children.add(this);
        transform.recalculateLocalTransformation();
    }

    public void removeChild(Entity e) {
        int i;
        for(i = 0; i < children.size(); i++) {
            if(children.get(i)==e) {
                removeChild(i);
                return;
            }
        }
    }

    public void removeChild(int v) {
        if(v >= children.size()) return;

        children.remove(v);
    }
    // Parent And Child Stuff

    public String toString() {
        if(!Objects.equals(name, "New Entity")) return "Entity(" + name + ")";
        else return "Entity(" + id + ")";
    }

    public void removeComponent(Component c) {
        components.remove(c);
    }

    public Component addComponent(Component c) {
        components.add(c);
        c.init(this);
        return c;
    }
}
