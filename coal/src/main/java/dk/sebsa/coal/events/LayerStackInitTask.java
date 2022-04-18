package dk.sebsa.coal.events;

import dk.sebsa.coal.asset.AssetExitsException;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.graph.Texture;
import dk.sebsa.coal.tasks.Task;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class LayerStackInitTask extends Task {
    private LayerStack stack;

    public LayerStackInitTask(LayerStack  stack) {
        this.stack = stack;
    }

    @Override
    protected String name() { return getClass().getSimpleName(); }

    @Override
    public void run() {
        stack.init();
    }
}
