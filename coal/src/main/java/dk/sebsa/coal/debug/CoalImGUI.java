package dk.sebsa.coal.debug;


import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class CoalImGUI {
    private static ImGuiImplGl3 implGl3;
    private static CoalImGUIGlfwImpl implGlfw;
    private static void log(Object o) { Coal.logger.log(o.toString(), "ImGUI"); }

    public static void init(Application app) {
        log("ImGUI init");

        // Creaete Context
        ImGui.createContext();

        // Create OpenGL and GLFW implementations
        implGlfw = new CoalImGUIGlfwImpl();
        implGlfw.init(app.window.getID(), false);
        implGl3 = new ImGuiImplGl3();
        implGl3.init("#version 150");

        ImGui.init();
        ImGui.styleColorsDark();
    }

    public static void cleanup() {
        log("ImGUI clean");
        implGlfw.dispose();
        implGl3.dispose();
        ImGui.destroyContext();
    }

    /**
     * @return the implGl3
     */
    public static ImGuiImplGl3 getImplGl3() {
        return implGl3;
    }

    /**
     * @return the implGlfw
     */
    public static CoalImGUIGlfwImpl getImplGlfw() {
        return implGlfw;
    }
}
