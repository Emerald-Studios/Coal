package dk.sebsa.arcade.layers.game1;

import dk.sebsa.arcade.layers.GameCreateUtil;
import dk.sebsa.coal.audio.AudioManager;
import dk.sebsa.coal.ecs.Component;
import dk.sebsa.coal.graph.renderes.Collision;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.math.Time;
import dk.sebsa.coal.util.Random;

/**
 * @author sebs
 */
public class Enemy extends Component {
    private static final float speed = 90;
    private int direction;

    @Override
    protected void load() {
        direction = Random.getInt(0,2);
        if(direction == 0) direction = -1;
    }

    @Override
    protected void update(GLFWInput input) {
        move2D(speed * direction * Time.getDeltaTime() * GameCreateUtil.doom, 0);
        if(transform.getPosition().x < -600) direction = 1;
        else if(transform.getPosition().x > 600) direction = -1;
    }

    @Override
    protected void lateUpdate(GLFWInput input) {

    }

    @Override
    public void onCollision2D(Collision collision) {
        if(collision.collider().getEntity().tag.equals("Bullet")) { entity.destroy(); collision.collider().getEntity().destroy();
            AudioManager.playSound(PlayerController.hit, 1);
        }
    }
}
