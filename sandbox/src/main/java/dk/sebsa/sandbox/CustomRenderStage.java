package dk.sebsa.sandbox;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.graph.FBO;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.RenderStage;
import dk.sebsa.coal.graph.Texture;
import dk.sebsa.coal.graph.renderes.Core2D;

public class CustomRenderStage extends RenderStage {
    public String getName() { return getClass().getSimpleName(); }

    public CustomRenderStage(Application app) {
        super(app);
    }

    private static Rect r = new Rect(0,0,600,600);
    private static final Rect r2 = new Rect(0,0,1,1);

    @Override
    protected void draw(FBO prevFBO) {
        if(app.window.isDirty()) r.set(0,0,app.window.getWidth(), app.window.getHeight());
        renderPrevFBO(prevFBO);
        Core2D.prepare();
        Core2D.drawTextureWithTextCoords((Texture) AssetManager.getAsset("sandboxassets/Creeppe.png"), r, r2);
        Core2D.unprepare();
    }
}