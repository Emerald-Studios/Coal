package dk.sebsa.coal.ecs;

import dk.sebsa.coal.math.Matrix4x4f;
import dk.sebsa.coal.math.Vector2f;
import dk.sebsa.coal.math.Vector3f;
import lombok.Getter;

/**
 * @author sebs
 * @since 1.0.0
 */
public class Transform {
    @Getter
    private boolean isDirty = true;
    protected final Entity entity;
    private final Matrix4x4f matrix = new Matrix4x4f();
    private Transform parent;

    protected Transform(Entity entity) {
        this.entity = entity;
        recalculateGlobalTransformations();
    }

    private final Vector3f position = new Vector3f();
    private final Vector3f localPosition = new Vector3f();

    // Getter & "Setters"
    public Vector3f getGlobalPosition() { return position; }
    public Vector3f getPosition() { return localPosition; }
    public void setPosition(Vector3f pos) {this.localPosition.set(pos); isDirty = true; recalculateGlobalTransformations(); }
    public void setPosition(float x, float y, float z) { this.localPosition.set(x, y, x); isDirty = true; recalculateGlobalTransformations(); }
    public void setPosition(float v) { this.localPosition.set(v, v, v); isDirty = true; recalculateGlobalTransformations(); }

    protected void recalculateLocalTransformation() {
        parent = entity.getParent().transform;
        isDirty = true;
        localPosition.set(parent.position.x - position.x, parent.position.y - position.y, parent.position.z - position.z);

        for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }

    protected void recalculateGlobalTransformations() {
        parent = entity.getParent().transform;
        isDirty = true;
        position.set(parent.position.x + localPosition.x, parent.position.y + localPosition.y, parent.position.z + localPosition.z);

        for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }

    public static void recalculate() {for(int i = 0; i < Entity.master.getChildren().size(); i++) Entity.master.getChildren().get(i).transform.recalculateGlobalTransformations();}

    private final Vector2f pos2D = new Vector2f();
    public Matrix4x4f getMatrix2D() {
        if(isDirty) matrix.setTransformation(pos2D.set(position.x, position.y), 0, Vector2f.VECTOR2F_ONE);
        isDirty = false;
        return matrix;
    }
}
