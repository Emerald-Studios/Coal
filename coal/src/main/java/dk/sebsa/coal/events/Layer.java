package dk.sebsa.coal.events;

import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.stoneui.Element;
import dk.sebsa.coal.stoneui.elements.Box;
import dk.sebsa.coal.stoneui.elements.ImGUIElement;
import dk.sebsa.coal.tasks.ThreadLogging;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public abstract class Layer {
    public boolean enabled = true;
    public List<Element> elements;
    public SpriteSheet preferredSpriteSheet;

    protected void log(Object o) { ThreadLogging.log(o.toString(), this.getClass().getSimpleName()); }

    protected abstract boolean handleEvent(Event e);
    protected abstract void init();
    protected abstract void update();
    protected abstract void cleanup();
    protected abstract SpriteSheet buildUI();
    protected void fullBuildUI() {
        elements = new ArrayList<>();
        preferredSpriteSheet = buildUI();
    }

    // ELEMENTS
    protected ImGUIElement ImGUI(Supplier<Void> drawFunction) {
        ImGUIElement e = new ImGUIElement(drawFunction);
        elements.add(e);
        return e;
    }

    protected Box Box() {
        Box e = new Box();
        elements.add(e);
        return e;
    }
}
