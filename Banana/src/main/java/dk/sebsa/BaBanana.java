package dk.sebsa;

import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.ecs.Entity;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;

/**
 * @author sebs
 */
public class BaBanana extends Layer {
    @Override
    protected boolean handleEvent(Event e) {
        return false;
    }

    @Override
    protected void init() {
        Entity player = new Entity("Player");
        Sprite sprPlayer = AssetManager.getAsset("banban/plr/Player.spr");
        player.addComponent(new SpriteRenderer(sprPlayer));
    }

    @Override
    protected void update() {

    }

    @Override
    protected void cleanup() {

    }

    @Override
    protected SpriteSheet buildUI() {
        return null;
    }
}
