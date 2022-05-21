package dk.sebsa.arcade.layers;

import dk.sebsa.Arcade;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.ecs.Entity;
import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.graph.text.Font;
import dk.sebsa.coal.graph.text.Label;
import dk.sebsa.coal.io.KeyPressedEvent;
import dk.sebsa.coal.math.Color;
import org.lwjgl.glfw.GLFW;

/**
 * @author sebs
 */
public class MainLogicLayer extends Layer {
    private final Application application;
    private SpriteSheet sheet;
    private Font font;
    private Label label;

    public MainLogicLayer(Application application) {
        this.application = application;
    }

    public void llog(Object o) {
        log(o);
    }

    @Override
    protected boolean handleEvent(Event e) {
        if(e.eventType().equals(EventTypes.KeyPressed)) {
            KeyPressedEvent e2 = (KeyPressedEvent) e;
            if(e2.key == GLFW.GLFW_KEY_F3) Arcade.instance.debugLayer.enabled = !Arcade.instance.debugLayer.enabled;
        }
        return false;
    }

    @Override
    protected void init() {
        sheet = (SpriteSheet) AssetManager.getAsset("internal/sheets/BlackGUI.sht");
        font = (Font) AssetManager.getAsset("arcadeassets/Doom.fnt");
        GameCreateUtil.createSpaceInvaders();
    }

    @Override
    protected void update() {
        if(GameCreateUtil.doom < 14) {
            GameCreateUtil.frameSpaceInvaders();
        } else if(GameCreateUtil.alive) {
            GameCreateUtil.alive = false;
            Entity.destroyAll();
        }
    }

    private static final Rect r = new Rect(0,0,400,400);
    @Override
    protected void render() {
        if(GameCreateUtil.doom > 3.5f) label = new Label("DOOM", font, Color.red);
        else label = new Label("Doom: " + GameCreateUtil.doom, font, Color.white);

        GUI.prepare(sheet, application);

        GUI.label(r, label);

        GUI.unprepare();
    }

    @Override
    protected void cleanup() {

    }
}
