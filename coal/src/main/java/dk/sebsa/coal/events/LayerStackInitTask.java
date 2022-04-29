package dk.sebsa.coal.events;

import dk.sebsa.coal.tasks.Task;

/**
 * @author sebs
 * @since 1.0.0
 */
public class LayerStackInitTask extends Task {
    private final LayerStack stack;

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
