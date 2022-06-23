package dk.sebsa.coal.ecs;

import dk.sebsa.coal.graph.renderes.Collision;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.math.Vector2f;
import dk.sebsa.coal.math.Vector3f;
import dk.sebsa.coal.trash.Trash;
import lombok.Getter;

/**
 * @author sebs
 * @since 1.0.0
 */
public abstract class Component extends Trash {
    @Getter protected Entity entity;
    protected Transform transform;
    @Getter private boolean enabled = true;

    protected abstract void load();
    protected abstract void update(GLFWInput input);
    protected abstract void lateUpdate(GLFWInput input);

    void init(Entity entity) {
        this.entity = entity;
        this.transform = entity.transform;
        load();
    }
    protected void move2D(Vector2f v) { move3D(v.x, v.y, 0); }
    protected void move2D(float x, float y) { move3D(x, y, 0); }

    protected void move3D(Vector3f v) { move3D(v.x, v.y, v.z); }
    protected void move3D(float x, float y, float z) {
        transform.setPosition(transform.getPosition().x+x, transform.getPosition().y+y, transform.getPosition().z+z);
    }

    public void destroy() {
        // DO NOTHING!!
    }

    public void onCollision2D(Collision collision) {
        // DO NOTHING!!
    }

    public void onTrigger2D(Collision collision) {
        // DO NOTHING!!
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
