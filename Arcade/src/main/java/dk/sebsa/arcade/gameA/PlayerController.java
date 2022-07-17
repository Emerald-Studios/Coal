package dk.sebsa.arcade.gameA;

import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.audio.AudioManager;
import dk.sebsa.coal.audio.Sound;
import dk.sebsa.coal.ecs.Component;
import dk.sebsa.coal.ecs.Entity;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.math.Time;
import dk.sebsa.coal.physm.M2D.MAABBCollider2D;
import org.lwjgl.glfw.GLFW;

/**
 * @author sebs
 */
public class PlayerController extends Component {
    public final float speed = 480;
    public final float cooldown = 0.4f;

    private float timer = 0.4f;

    private Sprite bulletSprite;
    public static Sound attack, hit;

    @Override
    protected void load() {
        bulletSprite = (Sprite) AssetManager.getAsset("arcadeassets/game1/bullet.spr");
        attack = (Sound) AssetManager.getAsset("arcadeassets/game1/Explosion1.ogg");
        hit = (Sound) AssetManager.getAsset("arcadeassets/game1/Explosion2.ogg");
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
                explosion.addComponent(new MAABBCollider2D(sr).setTrigger(true));
                AudioManager.playSound(attack, 1);
            }
        }
    }

    @Override
    protected void lateUpdate(GLFWInput input) {

    }
}
