package dk.sebsa.arcade.gameA;

import dk.sebsa.coal.audio.AudioManager;
import dk.sebsa.coal.ecs.Component;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.math.Time;
import dk.sebsa.coal.physm.M2D.MCollider2D;
import dk.sebsa.coal.util.Random;

/**
 * @author sebs
 */
public class Enemy extends Component {
    private static final float speed = 90;
    private int direction;
    private float collisionTimer = 0;
    private final GameA gameA;

    public Enemy(GameA gameA) {
        this.gameA = gameA;
    }

    @Override
    protected void load() {
        direction = Random.getInt(0,2);
        if(direction == 0) direction = -1;
    }

    @Override
    protected void update(GLFWInput input) {
        move2D(speed * direction * Time.getDeltaTime() * gameA.doom, 0);
        if(transform.getPosition().x < -600) direction = 1;
        else if(transform.getPosition().x > 600) direction = -1;
        collisionTimer -= Time.getDeltaTime();
    }

    @Override
    protected void lateUpdate(GLFWInput input) {

    }

    @Override
    public void onCollision2D(MCollider2D collider) {
        if(collider.getEntity().tag.equals("Enemy") && collisionTimer <= 0) {
            direction = direction * -1;
            collisionTimer = 1;
        }
    }

    @Override
    public void onTrigger2D(MCollider2D collider) {
        if(collider.getEntity().tag.equals("Bullet")) {
            entity.destroy(); collider.getEntity().destroy();
            AudioManager.playSound(PlayerController.hit, 1);
        }
    }
}
