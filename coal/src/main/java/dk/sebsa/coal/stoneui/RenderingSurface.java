package dk.sebsa.coal.stoneui;

import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.stoneui.elements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author sebs
 */
public abstract class RenderingSurface {
    public Group elementGroup;
    public SpriteSheet preferredSpriteSheet;
    protected abstract SpriteSheet buildUI();
    public void fullBuildUI(Application app) {
        elementGroup = new Group();
        elementGroup.size(app.window.rect.width, app.window.rect.height);
        preferredSpriteSheet = buildUI();
        if(Coal.TRACE) Coal.logger.log("Full UI Gen for Surface: " + getClass().getSimpleName() + ", DONE!");
    }

    protected void dirty() { elementGroup.dirty(); }

    // ELEMENTS
    protected List<Group> gqueue = new ArrayList<>();
    private Element element(Element e) {
        Group g = cg();
        g.addToGroup(e);
        e.parent = g;
        return e;
    }

    private Group cg() {
        if(gqueue.isEmpty()) return elementGroup;
        else return gqueue.get(0);
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
        cg().elements.remove(text);
        Button e = new Button(text, action);
        return (Button) element(e);
    }

    protected Button Button(Consumer<Button> action) {
        Button e = new Button(action);
        return (Button) element(e);
    }

    protected <T extends Element<T>> Element<T> AdvancedElement(T element) {
        return (T) element(element);
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

    protected HList HList(Supplier<Void> listBuilder) {
        HList g = new HList();
        element(g);
        gqueue.add(0, g);

        listBuilder.get();

        gqueue.remove(0);
        return g;
    }

    protected Group customElement(CustomElement e) {
        e.buildUI();
        element(e.elementGroup);
        return e.elementGroup;
    }

    private final Map<String, State> persistantStates = new HashMap<>();

    protected State getSate(String key) {
        return persistantStates.computeIfAbsent(key, (s) ->  new State() );
    }
}
