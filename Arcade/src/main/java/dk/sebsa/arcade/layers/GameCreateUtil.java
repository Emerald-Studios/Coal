package dk.sebsa.arcade.layers;

import dk.sebsa.arcade.layers.game1.Enemy;
import dk.sebsa.arcade.layers.game1.PlayerController;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.ecs.Entity;
import dk.sebsa.coal.ecs.collision.BoxCollider2D;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;

/**
 * @author sebs
 */
public class GameCreateUtil {
    private static Sprite enemySprite, playerSprite;
    private static boolean init = false;

    private static void init() {
        if(init) return; init = true;
        playerSprite = (Sprite) AssetManager.getAsset("arcadeassets/game1/player.spr");
        enemySprite = (Sprite) AssetManager.getAsset("arcadeassets/game1/zote.spr");
    }

    public static void createSpaceInvaders() {
        init();
        Entity.destroyAll();

        SpriteRenderer sr = new SpriteRenderer(playerSprite);
        sr.scale = 0.25f;
        Entity player = new Entity("Player"); player.tag = "Player";
        player.addComponent(new PlayerController());
        player.addComponent(sr);
        player.transform.setPosition(0,-350,0);

        for(int i = 0; i < 4; i++) {
            spawnEnemy(i);
        }
    }

    private static void spawnEnemy(int i) {
        init();

        SpriteRenderer sr = new SpriteRenderer(enemySprite);
        sr.scale = 0.4f;

        Entity zote = new Entity(); zote.tag = "Enemy";
        zote.addComponent(sr);
        zote.addComponent(new BoxCollider2D(sr));
        zote.addComponent(new Enemy());
        if(i == 0) zote.transform.setPosition(-150,0,0);
        else if(i == 1) zote.transform.setPosition(-50,0,0);
        else if(i == 2) zote.transform.setPosition(50,0,0);
        else if(i == 3) zote.transform.setPosition(150,0,0);
    }
}
