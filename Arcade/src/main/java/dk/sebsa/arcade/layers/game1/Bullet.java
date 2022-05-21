package dk.sebsa.arcade.layers.game1;

import dk.sebsa.coal.ecs.Component;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.math.Time;

/**
 * @author sebs
 */
public class Bullet extends Component {
    public float speed = 645;
    private float countdown = 4;

    @Override
    protected void load() {

    }

    @Override
    protected void update(GLFWInput input) {
        move2D(0, speed * Time.getDeltaTime());
        countdown -= Time.getDeltaTime();
        if(countdown <= 0) entity.destroy();
    }

    @Override
    protected void lateUpdate(GLFWInput input) {

    }
}
