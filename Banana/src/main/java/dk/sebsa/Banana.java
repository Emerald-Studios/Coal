package dk.sebsa;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.asset.FolderAssetProvider;
import dk.sebsa.coal.graph.RenderPipeline;
import dk.sebsa.coal.graph.stages.RenderSprites;
import dk.sebsa.coal.io.GLFWWindow;
import dk.sebsa.coal.math.Color;

/**
 * @author sebs
 */
public class Banana extends Application {
    @Override
    public String getName() {
        return "Project Banana!";
    }

    @Override
    public String getAuthor() {
        return "sebs@sebsa.dk";
    }

    @Override
    public String getVersion() {
        return "1.0.0-SNAPSHOT";
    }

    @Override
    protected GLFWWindow initApp() {
        AssetManager.addAssetProvider(new FolderAssetProvider("banban/"));

        stack.stack.add(new BaBanana());
        renderPipeline = new RenderPipeline.RenderPipelineBuilder().appendStage(new RenderSprites(this)).build();

        return new GLFWWindow(getName(), Color.yellow, 366, 740);
    }

    public static void main(String[] args) {
        Coal.fireUp(new Banana(), CoalCapabilities.builder()
                        .coalDebug(true)
                        .coalSprite2D(true)
                .build());
    }
}
