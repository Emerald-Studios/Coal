package dk.sebsa.arcade.layers.game1;

import dk.sebsa.coal.ecs.Component;
import dk.sebsa.coal.graph.renderes.Collision;
import dk.sebsa.coal.io.GLFWInput;

/**
 * @author sebs
 */
public class Enemy extends Component {
    @Override
    protected void load() {

    }

    @Override
    protected void update(GLFWInput input) {

    }

    @Override
    protected void lateUpdate(GLFWInput input) {

    }

    @Override
    public void onCollision2D(Collision collision) {
        if(collision.collider().getEntity().tag.equals("Bullet")) entity.destroy();
    }
}
