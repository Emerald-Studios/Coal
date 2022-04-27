package dk.sebsa.coal.graph.stages;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.graph.FBO;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.RenderStage;
import dk.sebsa.coal.graph.Texture;
import dk.sebsa.coal.graph.renderes.Render2D;

public class TestStage extends RenderStage {
    public String getName() { return getClass().getSimpleName(); }

    public TestStage(Application app) {
        super(app);
    }

    private static final Rect r = new Rect(0, 1, 1, -1);

    @Override
    protected void draw(FBO prevFBO) {
        renderPrevFBO(prevFBO);
        Render2D.prepare();
        Render2D.drawTextureWithTextCoords((Texture) AssetManager.getAsset("internal/textures/Chicken.png"), new Rect(0,0,app.window.getWidth(),app.window.getHeight()), new Rect(0,0,1,1));
        Render2D.unprepare();
    }
}
