package dk.sebsa.coal.events;


import dk.sebsa.Coal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebs
 * @since 1.0.0
 */
public class LayerStack {
    public final List<Layer> stack = new ArrayList<>();
    private final List<Event> queue = new ArrayList<>();
    private int i, j = 0;
    public void event(Event e) { queue.add(e); }

    public void init() {
        Coal.logger.log("Layerstack Init", "LayerStack");
        for(i = 0; i < stack.size(); i++) {
            stack.get(i).init();
        }
    }

    public void cleanup() {
        Coal.logger.log("Layerstack Cleanup", "LayerStack");
        for(i = 0; i < stack.size(); i++) {
            stack.get(i).cleanup();
        }
    }

    public void update() {
        for(j = 0; j < stack.size(); j++) {
            if(stack.get(j).enabled) stack.get(j).update();
        }
    }

    public void render() {
        for(i = 0; i < stack.size(); i++) {
            if(stack.get(i).enabled) stack.get(i).render();
        }
    }

    public void handleEvents() {
        while (!queue.isEmpty()) {
            Event event = queue.get(0);
            queue.remove(0);

            for(i = 0; i < stack.size(); i++) {
                if(!stack.get(i).enabled) continue;
                if(stack.get(i).handleEvent(event)) break;
            }
        }
    }
}

