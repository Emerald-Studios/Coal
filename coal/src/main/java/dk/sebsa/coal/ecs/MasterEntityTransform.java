package dk.sebsa.coal.ecs;

import dk.sebsa.coal.math.Vector3f;

public class MasterEntityTransform extends Transform {
    protected MasterEntityTransform(Entity entity) {
        super(entity);
    }

    public void setPosition(Vector3f pos) { }
    public void setPosition(float x, float y, float z) {  }
    public void setPosition(float x) {  }


    protected void recalculateLocalTransformation() {
        for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }

    protected void recalculateGlobalTransformations() {
        for(int i = 0; i < entity.getChildren().size(); i++) entity.getChildren().get(i).transform.recalculateGlobalTransformations();
    }
}
