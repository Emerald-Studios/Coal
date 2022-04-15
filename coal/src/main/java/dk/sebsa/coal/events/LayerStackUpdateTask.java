package dk.sebsa.coal.events;

import dk.sebsa.coal.tasks.Task;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class LayerStackUpdateTask extends Task {
    private LayerStack stack;

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
