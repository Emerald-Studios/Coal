package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.debug.CoalImGUI;
import dk.sebsa.coal.stoneui.Element;
import imgui.ImGui;

import java.util.function.Supplier;

/**
 * @author sebs
 */
public class ImGUIElement extends Element<ImGUIElement> {
    private final Supplier<Void> drawFunction;

    public ImGUIElement(Supplier<Void> drawFunction) {
        this.drawFunction = drawFunction;
    }

    @Override
    public void render() {
        // Start new frame
        CoalImGUI.getImplGlfw().newFrame();
        ImGui.newFrame();

        // Draw the frame
        drawFunction.get();

        // Actually render this shit
        ImGui.render();
        CoalImGUI.getImplGl3().renderDrawData(ImGui.getDrawData());

    }
}
