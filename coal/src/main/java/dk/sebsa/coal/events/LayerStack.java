package dk.sebsa.coal.events;


import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.renderes.GUI;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebs
 * @since 1.0.0
 */
public class LayerStack {
    public final List<Layer> stack = new ArrayList<>();
    private final List<Event> queue = new ArrayList<>();
    private int i;
    private final Application app;

    public LayerStack(Application app) {
        this.app = app;
    }

    public void event(Event e) { queue.add(e); }

    public void init() {
        Coal.logger.log("Layerstack Init " + stack.size(), "LayerStack");
        for(i = 0; i < stack.size(); i++) {
            stack.get(i).init();
            stack.get(i).fullBuildUI(app);
        }
    }

    public void cleanup() {
        Coal.logger.log("Layerstack Cleanup", "LayerStack");
        for(i = 0; i < stack.size(); i++) {
            stack.get(i).cleanup();
        }
    }

    public void update() {
        for(int j = 0; j < stack.size(); j++) {
            if(stack.get(j).enabled) stack.get(j).update();
        }
    }

    public void render() {
        Layer l;

        for(i = 0; i < stack.size(); i++) {
            l = stack.get(i);

            if(app.window.isDirty()) l.elementGroup = null;
            if(l.enabled) {
                if(l.elementGroup == null || l.elementGroup.dirty) l.fullBuildUI(app);
                if(l.preferredSpriteSheet != null) GUI.prepare(l.preferredSpriteSheet, app);

                l.elementGroup.draw(new Rect());
            }
        }
        GUI.unprepare();
    }

    public void handleEvents() {
        while (!queue.isEmpty()) {
            Event event = queue.get(0);
            queue.remove(0);

            for(i = 0; i < stack.size(); i++) {
                if(!stack.get(i).enabled) continue;
                if(stack.get(i).event(event)) break;
            }
        }
    }
}

