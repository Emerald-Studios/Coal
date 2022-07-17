package dk.sebsa.coal.physm.M2D;

import dk.sebsa.coal.ecs.Component;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.math.Vector2f;
import dk.sebsa.coal.physm.MCollision2D;

/**
 * @author sebs
 */
public class MCollider2D extends Component {
    public boolean isTrigger = false;
    public Vector2f anchor = new Vector2f(-0.5f, -0.5f);

    @Override
    protected void load() { /* EEE */}

    @Override
    protected void update(GLFWInput input) { }

    @Override
    protected void lateUpdate(GLFWInput input) {
        if(transform.isDirty()) MCollision2D.movers.add(this);
        else MCollision2D.solids.add(this);
    }
}
