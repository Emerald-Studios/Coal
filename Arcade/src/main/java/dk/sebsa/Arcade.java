package dk.sebsa;

import dk.sebsa.arcade.layers.DebugLayer;
import dk.sebsa.arcade.layers.MainLogicLayer;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.asset.FolderAssetProvider;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.graph.RenderPipeline;
import dk.sebsa.coal.graph.stages.RenderColliders;
import dk.sebsa.coal.graph.stages.RenderSprites;
import dk.sebsa.coal.io.GLFWWindow;
import dk.sebsa.coal.math.Color;

/**
 * @author sebs
 */
public class Arcade extends Application {
    public Layer mainLogicLayer, debugLayer;
    public static Arcade instance;

    public String getName() { return "Coal Arcade Demo"; }
    public String getAuthor() { return "Sebsa"; }
    public String getVersion() { return "1.0.0-SNAPSHOT"; }

    public static void main(String[] args) {
        Coal.fireUp(new Arcade(), CoalCapabilities.builder()
                        .coalSprite2D(true)
                        .coalPhysics2D(true)
                        .coalDebug(true)
                        .build());
    }

    @Override
    protected GLFWWindow initApp() {
        instance = this;
        AssetManager.addAssetProvider(new FolderAssetProvider("arcadeassets/"));

        mainLogicLayer = new MainLogicLayer();
        debugLayer = new DebugLayer(this);
        debugLayer.enabled = false;

        stack.stack.add(mainLogicLayer);
        stack.stack.add(debugLayer);

        renderPipeline = new RenderPipeline.RenderPipelineBuilder()
                .appendStage(new RenderSprites(this))
                .appendStage(new RenderColliders(this))
                .build();

        return new GLFWWindow("Coal Arcade", Color.black, 1200, 900);
    }
}
