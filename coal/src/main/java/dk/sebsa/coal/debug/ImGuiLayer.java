package dk.sebsa.coal.debug;


import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.io.*;
import dk.sebsa.coal.math.Color;
import dk.sebsa.coal.math.Time;
import imgui.ImGui;
import imgui.ImGuiIO;

import java.text.DecimalFormat;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public abstract class ImGuiLayer extends Layer {
    private final Application app;
    private final boolean selfDestruct;
    protected abstract boolean disableDefaultWindows();
    protected abstract void draw();

    public ImGuiLayer(Application app) {
        this.app = app;
        selfDestruct = !Coal.DEBUG;
    }

    @Override
    protected boolean handleEvent(Event e) {
        if(selfDestruct) return false;
        // Send input data to ImGUI
        final ImGuiIO io = ImGui.getIO();
        if(e.eventType() == EventTypes.MouseMove) {
            MouseMoveEvent e2 = (MouseMoveEvent) e;
            io.setMousePos((float) e2.mousePosX[0], (float) e2.mousePosY[0]);
        } else if(e.eventType() == EventTypes.ButtonPressed) {
            ButtonPressedEvent e2 = (ButtonPressedEvent) e;
            io.setMouseDown(e2.button, true);
        } else if(e.eventType() == EventTypes.ButtonReleased) {
            ButtonReleasedEvent e2 = (ButtonReleasedEvent) e;
            io.setMouseDown(e2.button, false);
        } else if(e.eventType() == EventTypes.MouseScroll) {
            MouseScrollEvent e2 = (MouseScrollEvent) e;
            io.setMouseWheelH(io.getMouseWheelH() + (float) e2.offsetX);
            io.setMouseWheel(io.getMouseWheel() + (float) e2.offsetY);
        } else if(e.eventType() == EventTypes.KeyPressed) {
            KeyPressedEvent e2 = (KeyPressedEvent) e;
            io.setKeysDown(e2.key, true);
        } else if(e.eventType() == EventTypes.KeyReleased) {
            KeyReleasedEvent e2 = (KeyReleasedEvent) e;
            io.setKeysDown(e2.key, false);
        } /*else if(e.eventType() == EventType.CharEvent) {
			CharEvent e2 = (CharEvent) e;
			io.addInputCharacter(e2.codePoint);
		}*/
        return false;
    }

    @Override
    protected void init() {
        if(selfDestruct) enabled = false;
    }

    @Override
    protected void update() {
        if(selfDestruct) enabled = false;
    }

    @Override
    protected void render() {
        if(selfDestruct) return;

        // Start new frame
        CoalImGUI.getImplGlfw().newFrame();
        ImGui.newFrame();

        // Draw the frame
        if(!disableDefaultWindows()) drawFrame();
        draw();

        // Actually render this shit
        ImGui.render();
        CoalImGUI.getImplGl3().renderDrawData(ImGui.getDrawData());
    }

    private void drawFrame() {
        DecimalFormat df = new DecimalFormat("#.#####");
        String aft = df.format(Time.getAFL());

        ImGui.begin("Engine Stats");
        ImGui.text("FPS: " + Time.getFPS() + "(" + aft + "ms)");
        ImGui.end();

        ImGui.begin("Debug Info");
        ImGui.text("Coal: " + Coal.COAL_VERSION);
        ImGui.text(app.toString());
        ImGui.newLine();
        ImGui.text("JAVA: V" + System.getProperty("java.version") + " from " + System.getProperty("java.vendor"));
        ImGui.text("OS: " + System.getProperty("os.name") + " V" + System.getProperty("os.version"));
        ImGui.text("DIR: " + System.getProperty("user.dir"));
        ImGui.text("PRC: " + System.getProperty("os.arch") + " " + Runtime.getRuntime().availableProcessors() + " Cores");
        ImGui.text("GRPH: " + Coal.graphicsCard);
        ImGui.end();

        // Console
        ImGui.begin("Console");

        ImGui.separator();

        ImGui.beginChild("Log");

        for(LogTracker.Log log : LogTracker.logs) {
            Color textColor;
            if(log.level() == 2) 		textColor = Color.red;
            else if(log.level() == 1)   textColor = Color.yellow;
            else                        textColor = Color.color(0.4078f, 1, 0.2f, 1);
            ImGui.textColored(textColor.r, textColor.g, textColor.b, 1f, log.s());
        }
        ImGui.endChild();
        ImGui.end();
    }

    @Override
    protected void cleanup() {
        // if(selfDestruct) return;
    }
}
