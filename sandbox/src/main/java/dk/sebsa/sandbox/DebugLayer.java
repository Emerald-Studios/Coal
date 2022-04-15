package dk.sebsa.sandbox;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.debug.ImGuiLayer;

public class DebugLayer extends ImGuiLayer {
    public DebugLayer(Application app) {
        super(app);
    }

    @Override
    protected void draw() {

    }

    @Override
    protected boolean disableDefaultWindows() {
        return false;
    }
}
