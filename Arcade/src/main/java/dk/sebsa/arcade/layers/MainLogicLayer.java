package dk.sebsa.arcade.layers;

import dk.sebsa.Arcade;
import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.io.KeyPressedEvent;
import org.lwjgl.glfw.GLFW;

/**
 * @author sebs
 */
public class MainLogicLayer extends Layer {
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
        GameCreateUtil.createSpaceInvaders();
    }

    @Override
    protected void update() {

    }

    @Override
    protected void render() {

    }

    @Override
    protected void cleanup() {

    }
}
