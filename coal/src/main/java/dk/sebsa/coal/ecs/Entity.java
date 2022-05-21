package dk.sebsa.coal.ecs;

import dk.sebsa.coal.trash.TrashCollector;
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
    private final List<Entity> children = new ArrayList<>();
    @Getter
    private final List<Component> components = new ArrayList<>();
    @Getter
    private Entity parent;
    public final Transform transform;
    public static final Entity master = new Entity(0);
    public String name = "New Entity";
    @Getter
    private final String id = UUID.randomUUID().toString();

    /**
     * The general tag used to easily identify groups of entities
     */
    public String tag = "Untagged";

    private Entity(int i) { transform = new MasterEntityTransform(this); name = "COAL-INTERNAL-MASTER"; }

    public Entity() {
        this.parent = master;
        master.children.add(this);
        transform = new Transform(this);
    }

    public Entity(String name) {
        this.parent = master;
        this.name = name;
        master.children.add(this);
        transform = new Transform(this);
    }

    public Entity(Entity parent) {
        this.parent = parent;
        this.parent.children.add(this);
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

    private void removeChild(Entity e) {
        int i;
        for(i = 0; i < children.size(); i++) {
            if(children.get(i)==e) {
                removeChild(i);
                return;
            }
        }
    }

    private void removeChild(int v) {
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
        TrashCollector.trash(c);
        c.entity = null;
    }

    public Component addComponent(Component c) {
        components.add(c);
        c.init(this);
        return c;
    }

    public void destroy() {
        parent = null;

        while(!children.isEmpty()) {
            children.get(0).destroy();
            children.remove(0);
        }

        while(!components.isEmpty()) {
            components.get(0).destroy();
            components.remove(0);
        }
    }

    public static void destroyAll() {
        while(!master.children.isEmpty()) {

            master.children.remove(0);
        }
    }
}
