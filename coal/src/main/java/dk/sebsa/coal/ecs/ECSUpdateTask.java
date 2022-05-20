package dk.sebsa.coal.ecs;

import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.graph.renderes.ColliderCalculationTask2D;
import dk.sebsa.coal.graph.renderes.Collision;
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
            for(Component component : e2.getComponents()) {
                if(component.isEnabled())
                    components.add(component);
            }

            recurseAddComponent(e2);
        }
    }

    @Override
    public void run() {
        recurseAddComponent(master);

        for(Component c : components) {
            c.update(input);
        }

        if(Coal.getCapabilities().coalPhysics2D) {
            try {
                ColliderCalculationTask2D.latch.await();
            } catch (InterruptedException e) { /* DO NOTHING */ }

            synchronized (ColliderCalculationTask2D.getCollision()) {
                for(Collision collision : ColliderCalculationTask2D.getCollision()) {
                    for(Component c : collision.main().entity.getComponents()) {
                        c.onCollision2D(collision);
                    }
                }
            }
        }

        for(Component c : components) {
            c.lateUpdate(input);
        }
    }
}
