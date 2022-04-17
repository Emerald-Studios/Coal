package dk.sebsa.sandbox;

import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.io.GLFWWindow;
import dk.sebsa.coal.math.Color;

public class Sandbox extends Application {
    public String getName() { return "Coal Sandbox"; }
    public String getAuthor() { return "Emerald Studios"; }
    public String getVersion() { return "1.0"; }

    public static void main(String[] args) {
        Coal.fireUp(new Sandbox(), true);
    }

    public Sandbox() {
        super(new GLFWWindow("Sandbox", Color.cyan(), 800, 600));
        stack.stack.add(new DebugLayer(this));
        stack.stack.add(new SandboxLayer(this));
    }
}
