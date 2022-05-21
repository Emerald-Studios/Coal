package dk.sebsa.arcade.layers;

import dk.sebsa.Arcade;
import dk.sebsa.arcade.layers.game1.Enemy;
import dk.sebsa.arcade.layers.game1.PlayerController;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.ecs.Entity;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;
import dk.sebsa.coal.math.Time;
import dk.sebsa.coal.physics.collision.BoxCollider2D;
import dk.sebsa.coal.util.Random;

/**
 * @author sebs
 */
public class GameCreateUtil {
    private static Sprite enemySprite, playerSprite;
    private static boolean init = false;
    public static boolean alive = false;
    
    private static void init() {
        if(init) return; init = true;
        playerSprite = (Sprite) AssetManager.getAsset("arcadeassets/game1/player.spr");
        enemySprite = (Sprite) AssetManager.getAsset("arcadeassets/game1/zote.spr");
    }

    public static void createSpaceInvaders() {
        init(); alive = true;
        Entity.destroyAll();

        SpriteRenderer sr = new SpriteRenderer(playerSprite);
        sr.scale = 0.25f;
        Entity player = new Entity("Player"); player.tag = "Player";
        player.addComponent(new PlayerController());
        player.addComponent(sr);
        player.transform.setPosition(0,-350,0);

        for(int i = 0; i < 4; i++) {
            spawnEnemy();
        }
    }

    private static float count = 7;
    public static float doomTimer = 56;
    public static float semiDoomTimer = 8;
    public static float doom = 0.67f;

    public static void frameSpaceInvaders() {
        if(count <= 0) {
            if(doom >= 4) count = 4;
            else count = Random.getFloat((int) (4-doom),9);
            Arcade.log("DOOOM " + doom);

            for(int i = 0; i < Random.getInt((int) (1*doom), (int) (5*doom)); i++) {
                spawnEnemy();
            }
        } else if(semiDoomTimer <= 0) {
            semiDoomTimer = 8;
            doom *= 1.26f;
        }


        count -= Time.getDeltaTime();
        semiDoomTimer -= Time.getDeltaTime();
    }

    private static void spawnEnemy() {
        init();

        SpriteRenderer sr = new SpriteRenderer(enemySprite);
        sr.scale = 0.35f;

        Entity zote = new Entity(); zote.tag = "Enemy";
        zote.addComponent(sr);
        zote.addComponent(new BoxCollider2D(sr));
        zote.addComponent(new Enemy());
        if(doom >= 3.5f) zote.transform.setPosition(Random.getInt(-600,500),-doom*17,0);
        else zote.transform.setPosition(Random.getInt(-600,500),0,0);
    }
}
