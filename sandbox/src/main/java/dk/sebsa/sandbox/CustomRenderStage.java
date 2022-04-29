package dk.sebsa.sandbox;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.graph.*;
import dk.sebsa.coal.graph.renderes.Core2D;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.graph.text.Font;
import dk.sebsa.coal.graph.text.Label;

public class CustomRenderStage extends RenderStage {
    public String getName() { return getClass().getSimpleName(); }

    public CustomRenderStage(Application app) {
        super(app);
    }

    private static final Rect r = new Rect(0,0,600,600);
    private static final Rect r2 = new Rect(0,0,1,1);
    private static final Rect r3 = new Rect(0,0,200,200);
    private static final Rect r4 = new Rect(200,0,200,200);

    @Override
    protected void draw(FBO prevFBO) {
        if(app.window.isDirty()) r.set(0,0,app.window.getWidth(), app.window.getHeight());
        Material m = (Material) AssetManager.getAsset("sandboxassets/Asuna.mat");
        Sprite s = (Sprite) AssetManager.getAsset("sandboxassets/True.sht/Sega");
        Sprite s2 = (Sprite) AssetManager.getAsset("sandboxassets/True.sht/Nintendo");
        SpriteSheet sheet = (SpriteSheet) AssetManager.getAsset("internal/sheets/BlackGUI.sht");
        Font font = (Font) AssetManager.getAsset("internal/Test.fnt");

        renderPrevFBO(prevFBO);
        Core2D.prepare();
        Core2D.drawTextureWithTextCoords(m, r, r2);
        Core2D.drawSprite(r3, s);
        Core2D.drawSprite(r4, s2);

        GUI.prepare(sheet, app);
        GUI.box(new Rect(0,200,200,200));
        if(GUI.buttonDown(new Rect(0,400,400,42), new Label("U wanna...", font)))  GUI.label(new Rect(0,442,50,42), new Label("Cum in me", font));
        GUI.unprepare(); // Will unprepare Core2D
    }
}