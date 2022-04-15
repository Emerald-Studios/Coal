package dk.sebsa.sandbox;


import dk.sebsa.coal.Application;
import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.io.KeyPressedEvent;
import org.lwjgl.glfw.GLFW;

/**
 * @author Sebsa
 *
 */
public class SandboxLayer extends Layer {
    private final Application application;

    public SandboxLayer(Application app) {
        this.application = app;
    }

    @Override
    public boolean handleEvent(Event e) {
        if(e.eventType().equals(EventTypes.KeyPressed)) {
            KeyPressedEvent e2 = (KeyPressedEvent) e;
            if(e2.key == GLFW.GLFW_KEY_F11) application.window.setFullscreen(!application.window.isFullscreen());
        }
        return false;
    }

    @Override
    protected void init() {

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
