package dk.sebsa.sandbox;

import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.asset.FolderAssetProvider;
import dk.sebsa.coal.graph.RenderPipeline;
import dk.sebsa.coal.io.GLFWWindow;
import dk.sebsa.coal.math.Color;

public class Sandbox extends Application {
    protected DebugLayer debugLayer;
    protected SandboxLayer sandboxLayer;
    protected static Sandbox instance;

    public String getName() { return "Coal Sandbox"; }
    public String getAuthor() { return "Emerald Studios"; }
    public String getVersion() { return "1.0"; }

    public static void main(String[] args) { Coal.fireUp(new Sandbox(), true); }

    @Override
    public GLFWWindow initApp() {
        instance = this;

        debugLayer = new DebugLayer(this);
        sandboxLayer = new SandboxLayer(this);

        stack.stack.add(debugLayer);
        stack.stack.add(sandboxLayer);

        debugLayer.enabled = false;

        AssetManager.addAssetProvider(new FolderAssetProvider("sandboxassets/"));

        renderPipeline = new RenderPipeline.RenderPipelineBuilder()
                .appendStage(new CustomRenderStage(this)).build();

        return new GLFWWindow("Sandbox", Color.cyan, 800, 600);
    }
}
