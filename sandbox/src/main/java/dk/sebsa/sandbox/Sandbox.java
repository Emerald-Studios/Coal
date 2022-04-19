package dk.sebsa.sandbox;

import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.graph.RenderPipeline;
import dk.sebsa.coal.graph.TestStage;
import dk.sebsa.coal.io.GLFWWindow;
import dk.sebsa.coal.math.Color;

public class Sandbox extends Application {
    protected final DebugLayer debugLayer;
    protected final SandboxLayer sandboxLayer;
    protected static Sandbox instance;

    public String getName() { return "Coal Sandbox"; }
    public String getAuthor() { return "Emerald Studios"; }
    public String getVersion() { return "1.0"; }

    public static void main(String[] args) {
        Coal.fireUp(new Sandbox(), true);
    }

    public Sandbox() {
        super(new GLFWWindow("Sandbox", Color.cyan(), 800, 600));
        instance = this;

        debugLayer = new DebugLayer(this);
        sandboxLayer = new SandboxLayer(this);

        stack.stack.add(debugLayer);
        stack.stack.add(sandboxLayer);

        debugLayer.enabled = false;

        renderPipeline = new RenderPipeline.RenderPipelineBuilder().appendStage(new TestStage()).build();
    }
}
