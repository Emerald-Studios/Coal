package dk.sebsa.coal.ecs;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.tasks.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebs
 */
public class ECSUpdateTask extends Task {
    private final Entity master;
    private final GLFWInput input;
    private final List<Component> components = new ArrayList<>();

    public ECSUpdateTask(Entity master, Application app) {
        this.master = master;
        this.input = app.input;
    }

    @Override
    protected String name() { return "UpdateECS"; }

    public void recurseAddComponent(Entity e) {
        for(Entity e2 : e.getChildren()) {
            components.addAll(e2.getComponents());
            recurseAddComponent(e2);
        }
    }

    @Override
    public void run() {
        recurseAddComponent(master);

        for(Component c : components) {
            c.update(input);
        }

        for(Component c : components) {
            c.lateUpdate(input);
        }
    }
}
