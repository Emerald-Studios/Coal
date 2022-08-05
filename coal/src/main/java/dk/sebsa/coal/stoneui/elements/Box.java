package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.stoneui.Element;

/**
 * @author sebs
 */
public class Box extends Element<Box> {
    @Override
    public void render() {
        if(this.sprite == null)
            GUI.box(rect);
        else GUI.box(rect, sprite);
    }
}
