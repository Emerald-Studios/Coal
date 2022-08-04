package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.stoneui.Element;

/**
 * @author sebs
 */
public class Box extends Element<Box> {
    @Override
    public void render() {
        GUI.box(rect);
    }
}
