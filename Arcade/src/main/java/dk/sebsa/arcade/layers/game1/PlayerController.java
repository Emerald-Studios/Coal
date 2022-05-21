package dk.sebsa.arcade.layers.game1;

import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.ecs.Component;
import dk.sebsa.coal.ecs.Entity;
import dk.sebsa.coal.ecs.collision.BoxCollider2D;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.math.Time;
import org.lwjgl.glfw.GLFW;

/**
 * @author sebs
 */
public class PlayerController extends Component {
    public final float speed = 480;
    public final float cooldown = 0.4f;

    private float timer = 0.4f;

    private Sprite bulletSprite;

    @Override
    protected void load() {
        bulletSprite = (Sprite) AssetManager.getAsset("arcadeassets/game1/bullet.spr");
    }

    @Override
    protected void update(GLFWInput input) {
        if(timer > 0) timer -= Time.getDeltaTime();

        if(input.isKeyDown(GLFW.GLFW_KEY_D)) move2D(speed* Time.getDeltaTime(),0);
        if(input.isKeyDown(GLFW.GLFW_KEY_A)) move2D(-speed* Time.getDeltaTime(),0);
        if(input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            if(timer <= 0) { timer = cooldown;
                SpriteRenderer sr = new SpriteRenderer(bulletSprite);
                sr.scale = 0.7f;

                Entity explosion = new Entity(); explosion.tag = "Bullet";
                explosion.transform.setPosition(transform.getPosition().x-50, transform.getPosition().y, 0);
                explosion.addComponent(new Bullet());
                explosion.addComponent(sr);
                explosion.addComponent(new BoxCollider2D(sr));
            }
        }
    }

    @Override
    protected void lateUpdate(GLFWInput input) {

    }
}
