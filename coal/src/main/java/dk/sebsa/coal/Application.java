package dk.sebsa.coal;

import dk.sebsa.Coal;
import dk.sebsa.coal.events.LayerStack;
import dk.sebsa.coal.graph.RenderPipeline;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.io.GLFWWindow;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public abstract class Application {
    public abstract String getName();
    public abstract String getAuthor();
    public abstract String getVersion();

    public GLFWWindow window;
    public LayerStack stack;
    public GLFWInput input;
    public RenderPipeline renderPipeline;

    public Application(GLFWWindow window) {
        this.window = window;
        this.stack = new LayerStack();
    }

    public void init() {
        window.init();
        input = new GLFWInput(this, Coal.logger);
        input.addCallbacks();
    }

    public void cleanup() {
        stack.cleanup();
        input.cleanup();
        window.cleanup();
    }

    public boolean shouldClose() { return glfwWindowShouldClose(window.getID()); }

    @Override
    public String toString() {
        return getName() + " by " + getAuthor() +", v" + getVersion() + " (" + getClass().getName() + ")";
    }
}
