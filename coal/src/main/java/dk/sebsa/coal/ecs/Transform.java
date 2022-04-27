package dk.sebsa.coal.ecs;

import dk.sebsa.coal.math.Vector3f;
import lombok.Getter;

public class Transform {
    @Getter
    private boolean isDirty = true;
    private final Entity entity;
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
    public void setPosition(float x) { this.localPosition.set(x, x, x); isDirty = true; recalculateGlobalTransformations(); }

    protected void recalculateLocalTransformation() {
        if(this.equals(Entity.master.transform)) {
            for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
            return;
        } parent = entity.getParent().transform;
        isDirty = true;
        localPosition.set(parent.position.x - position.x, parent.position.y - position.y, parent.position.z - position.z);

        for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }

    protected void recalculateGlobalTransformations() {
        if(this.equals(Entity.master.transform)) {
            for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
            return;
        } parent = entity.getParent().transform;
        isDirty = true;
        position.set(parent.position.x + localPosition.x, parent.position.y + localPosition.y, parent.position.z + localPosition.z);

        for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }

    public static void recalculate() {for(int i = 0; i < Entity.master.getChildren().size(); i++) Entity.master.getChildren().get(i).transform.recalculateGlobalTransformations();}
}