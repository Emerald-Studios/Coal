package dk.sebsa.sandbox;

import dk.sebsa.Coal;
import dk.sebsa.CoalCapabilities;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.asset.FolderAssetProvider;
import dk.sebsa.coal.graph.RenderPipeline;
import dk.sebsa.coal.graph.RenderStage;
import dk.sebsa.coal.graph.stages.RenderColliders;
import dk.sebsa.coal.graph.stages.RenderSprites;
import dk.sebsa.coal.io.GLFWWindow;
import dk.sebsa.coal.math.Color;

public class Sandbox extends Application {
    protected DebugLayer debugLayer;
    protected SandboxLayer sandboxLayer;
    protected static Sandbox instance;
    protected RenderStage debugRenderStage;

    public String getName() { return "Coal Sandbox"; }
    public String getAuthor() { return "Emerald Studios"; }
    public String getVersion() { return "1.0"; }

    public static void main(String[] args) {
        Coal.fireUp(new Sandbox(),
            CoalCapabilities.builder()
                    .coalTrace(true)
                    //.coalDebug(false)
                    .coalAudio(true)
                    .coalSprite2D(true)
                    .build());
    }

    @Override
    public GLFWWindow initApp() {
        instance = this;

        debugLayer = new DebugLayer(this);
        sandboxLayer = new SandboxLayer(this);

        stack.stack.add(sandboxLayer);
        stack.stack.add(debugLayer);

        debugLayer.enabled = false;

        AssetManager.addAssetProvider(new FolderAssetProvider("sandboxassets/"));

        if(Coal.DEBUG) {
            debugRenderStage = new RenderColliders(this);
            debugRenderStage.setEnabled(false);
            renderPipeline = new RenderPipeline.RenderPipelineBuilder()
                    .appendStage(new RenderSprites(this))
                    .appendStage(debugRenderStage).build();
        } else  renderPipeline = new RenderPipeline.RenderPipelineBuilder()
                .appendStage(new RenderSprites(this)) .build();


        return new GLFWWindow("Sandbox", Color.cyan, 800, 600);
    }
}
