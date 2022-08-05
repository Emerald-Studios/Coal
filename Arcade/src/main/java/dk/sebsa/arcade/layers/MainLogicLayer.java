package dk.sebsa.arcade.layers;

import dk.sebsa.Arcade;
import dk.sebsa.arcade.Game;
import dk.sebsa.arcade.gameA.GameA;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.io.KeyPressedEvent;
import org.lwjgl.glfw.GLFW;

/**
 * @author sebs
 */
public class MainLogicLayer extends Layer {
    private final Application application;
    private Game currentGame;

    public MainLogicLayer(Application application) {
        this.application = application;
        currentGame = new GameA();
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
        currentGame.init(application);
    }

    @Override
    protected void update() {
        currentGame.frame();
    }

    @Override
    protected void cleanup() {
        currentGame.cleanup();
    }

    @Override
    protected SpriteSheet buildUI() {
        return null;
    }
}
