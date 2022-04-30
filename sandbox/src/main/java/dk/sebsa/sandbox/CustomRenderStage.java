package dk.sebsa.sandbox;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.graph.*;
import dk.sebsa.coal.graph.renderes.Core2D;

public class CustomRenderStage extends RenderStage {
    public String getName() { return getClass().getSimpleName(); }

    public CustomRenderStage(Application app) {
        super(app);
    }

    private static final Rect r = new Rect(0,0,600,600);
    private static final Rect r2 = new Rect(0,0,1,1);
    private static final Rect r3 = new Rect(0,0,200,200);
    private static final Rect r4 = new Rect(200,0,200,200);
    private boolean init = false;

    private Material m;
    private Sprite s, s2;

    private void assets() {
        if(init) return;
        init = true;

        m = (Material) AssetManager.getAsset("sandboxassets/Asuna.mat");
        s = (Sprite) AssetManager.getAsset("sandboxassets/True.sht/Sega");
        s2 = (Sprite) AssetManager.getAsset("sandboxassets/True.sht/Nintendo");
    }

    @Override
    protected void draw(FBO prevFBO) {
        if(app.window.isDirty()) r.set(0,0,app.window.getWidth(), app.window.getHeight());
        assets();

        renderPrevFBO(prevFBO);
        Core2D.prepare();

        Core2D.drawTextureWithTextCoords(m, r, r2);
        Core2D.drawSprite(r3, s);
        Core2D.drawSprite(r4, s2);

        Core2D.unprepare();
    }
}