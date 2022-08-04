package dk.sebsa.coal.events;

import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.stoneui.Element;
import dk.sebsa.coal.stoneui.elements.*;
import dk.sebsa.coal.tasks.ThreadLogging;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public abstract class Layer {
    public boolean enabled = true;
    public Group elementGroup;
    public SpriteSheet preferredSpriteSheet;

    protected void log(Object o) { ThreadLogging.log(o.toString(), this.getClass().getSimpleName()); }

    protected abstract boolean handleEvent(Event e);
    protected boolean event(Event e) {
        if(elementGroup.handleEvent(e)) return true;

        return handleEvent(e);
    }

    protected abstract void init();
    protected abstract void update();
    protected abstract void cleanup();
    protected abstract SpriteSheet buildUI();
    protected void fullBuildUI(Application app) {
        elementGroup = new Group();
        elementGroup.size(app.window.rect.width, app.window.rect.height);
        preferredSpriteSheet = buildUI();
        if(Coal.TRACE) log("Full UI Gen for layer: " + getClass().getSimpleName() + ", DONE!");
    }

    protected void dirty() { elementGroup = null; }

    // ELEMENTS
    protected List<Group> gqueue = new ArrayList<>();
    private Element element(Element e) {
        Group g;
        if(gqueue.isEmpty()) g = elementGroup;
        else g = gqueue.get(0);

        g.addToGroup(e);
        e.parent = g;
        return e;
    }

    protected ImGUIElement ImGUI(Supplier<Void> drawFunction) {
        ImGUIElement e = new ImGUIElement(drawFunction);
        return (ImGUIElement) element(e);
    }

    protected Box Box() {
        Box e = new Box();
        return (Box) element(e);
    }

    protected Text Text(String text) {
        Text e = new Text(text);
        return (Text) element(e);
    }

    protected Button Button(Text text, Consumer<Button> action) {
        gqueue.get(0).elements.remove(text);
        Button e = new Button(text, action);
        return (Button) element(e);
    }

    // GROUP ELEMENTS
    protected Group Group(Supplier<Void> groupBuilder) {
        Group g = new Group();
        element(g);
        gqueue.add(0, g);

        groupBuilder.get();

        gqueue.remove(0);
        return g;
    }

    protected dk.sebsa.coal.stoneui.elements.List List(Supplier<Void> listBuilder) {
        dk.sebsa.coal.stoneui.elements.List g = new dk.sebsa.coal.stoneui.elements.List();
        element(g);
        gqueue.add(0, g);

        listBuilder.get();

        gqueue.remove(0);
        return g;
    }
}
