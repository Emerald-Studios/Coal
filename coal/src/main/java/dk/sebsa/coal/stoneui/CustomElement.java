package dk.sebsa.coal.stoneui;

import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.stoneui.elements.Group;

/**
 * @author sebs
 */
public abstract class CustomElement extends RenderingSurface {
    public CustomElement(State state) {
        this.state = state;
    }

    protected abstract void build();
    public final State state;

    @Override
    protected SpriteSheet buildUI() {
        elementGroup = new Group();
        build();
        return null;
    }
}
