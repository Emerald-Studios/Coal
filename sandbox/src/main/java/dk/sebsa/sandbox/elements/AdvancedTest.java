package dk.sebsa.sandbox.elements;

import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.stoneui.Element;

/**
 * @author sebs
 */
public class AdvancedTest extends Element<AdvancedTest> {
    @Override
    protected void render() {
        GUI.box(rect);
    }
}
