package dk.sebsa.coal.events;

import dk.sebsa.coal.tasks.Task;

/**
 * @author sebs
 * @since 1.0.0
 */
public class LayerStackUpdateTask extends Task {
    private final LayerStack stack;

    public LayerStackUpdateTask(LayerStack  stack) {
        this.stack = stack;
    }

    @Override
    protected String name() { return getClass().getSimpleName(); }

    @Override
    public void run() {
        stack.update();
    }
}
