package dk.sebsa.arcade.gameA;

import dk.sebsa.Arcade;
import dk.sebsa.arcade.Game;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.ecs.Entity;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;
import dk.sebsa.coal.graph.text.Font;
import dk.sebsa.coal.graph.text.Label;
import dk.sebsa.coal.math.Color;
import dk.sebsa.coal.math.Time;
import dk.sebsa.coal.physics.collision.BoxCollider2D;
import dk.sebsa.coal.util.Random;

/**
 * @author sebs
 */
public class GameA extends Game {
    private boolean init = false;
    private boolean alive = false;
    private Application application;

    // State
    private float count;
    public float doomTimer;
    public float semiDoomTimer;
    public float doom;

    // ASSETS
    private Sprite enemySprite, playerSprite;
    private SpriteSheet sheet;
    private Font font;
    private Label label;
    private static final Rect r = new Rect(0,0,400,400);

    @Override
    public void renderUI() {
        if(doom > 3.5f) label = new Label("DOOM", font, Color.red);
        else label = new Label("Doom: " + doom, font, Color.white);

        GUI.prepare(sheet, application);

        GUI.label(r, label);

        GUI.unprepare();
    }

    @Override
    public void cleanup() {
        Entity.destroyAll();
    }

    @Override
    public void frame() {
        if(doom < 14) {
            update();
        } else if(alive) {
            alive = false;
            Entity.destroyAll();
        }
    }

    private void update() {
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

    @Override
    public void init(Application app) {
        if(!init) { init = true;
            playerSprite = (Sprite) AssetManager.getAsset("arcadeassets/game1/player.spr");
            enemySprite = (Sprite) AssetManager.getAsset("arcadeassets/game1/zote.spr");
            sheet = (SpriteSheet) AssetManager.getAsset("internal/sheets/BlackGUI.sht");
            font = (Font) AssetManager.getAsset("arcadeassets/Doom.fnt");
        } alive = true;
        application = app;

        count = 7;
        doomTimer = 56;
        semiDoomTimer = 8;
        doom = 0.67f;

        // Create PLayer
        SpriteRenderer sr = new SpriteRenderer(playerSprite);
        sr.scale = 0.25f;
        Entity player = new Entity("Player"); player.tag = "Player";
        player.addComponent(new PlayerController());
        player.addComponent(sr);
        player.transform.setPosition(0,-350,0);

        // Spawn Initial Enemies
        for(int i = 0; i < 4; i++) {
            spawnEnemy();
        }
    }

    @Override
    public boolean alive() {
        return alive;
    }

    private void spawnEnemy() {
        SpriteRenderer sr = new SpriteRenderer(enemySprite);
        sr.scale = 0.35f;

        Entity zote = new Entity(); zote.tag = "Enemy";
        zote.addComponent(sr);
        zote.addComponent(new BoxCollider2D(sr));
        zote.addComponent(new Enemy(this));
        if(doom >= 3.5f) zote.transform.setPosition(Random.getInt(-600,500),-doom*17,0);
        else zote.transform.setPosition(Random.getInt(-600,500),0,0);
    }
}
