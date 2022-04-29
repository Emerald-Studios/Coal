package dk.sebsa.coal.graph.stages;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.graph.FBO;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.RenderStage;
import dk.sebsa.coal.graph.Texture;
import dk.sebsa.coal.graph.renderes.Core2D;

/**
 * @author sebs
 * @since 1.0.0
 */
public class TestStage extends RenderStage {
    public String getName() { return getClass().getSimpleName(); }

    public TestStage(Application app) {
        super(app);
    }

    private static final Rect r = new Rect(0,0,600,600);
    private static final Rect r2 = new Rect(0,0,1,1);

    @Override
    protected void draw(FBO prevFBO) {
        renderPrevFBO(prevFBO);
        Core2D.prepare();
        Core2D.drawTextureWithTextCoords((Texture) AssetManager.getAsset("internal/textures/Chicken.png"), r, r2);
        Core2D.unprepare();
    }
}
