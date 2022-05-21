package dk.sebsa.arcade.layers;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.debug.ImGuiLayer;

/**
 * @author sebs
 */
public class DebugLayer extends ImGuiLayer {
    public DebugLayer(Application app) {
        super(app);
    }

    @Override
    protected boolean disableDefaultWindows() { return false; }

    @Override
    protected void draw() {

    }
}
