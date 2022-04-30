package dk.sebsa.sandbox;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.graph.*;
import dk.sebsa.coal.graph.renderes.Core2D;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.graph.text.Font;
import dk.sebsa.coal.graph.text.Label;
import dk.sebsa.coal.math.Color;

public class CustomRenderStage extends RenderStage {
    public String getName() { return getClass().getSimpleName(); }

    public CustomRenderStage(Application app) {
        super(app);
    }

    private static final Rect r = new Rect(0,0,600,600);
    private static final Rect r2 = new Rect(0,0,1,1);
    private static final Rect r3 = new Rect(0,0,200,200);
    private static final Rect r4 = new Rect(200,0,200,200);
    private static final Rect r5 = new Rect(0,200,200,200);
    private static final Rect r6 = new Rect(0,400,400,42);
    private static final Rect r7 = new Rect(0,442,50,42);
    private boolean init = false;

    private Material m;
    private Sprite s, s2;
    private SpriteSheet sheet;
    private Font font;
    private Label label;

    private void assets() {
        if(init) return;
        init = true;

        m = (Material) AssetManager.getAsset("sandboxassets/Asuna.mat");
        s = (Sprite) AssetManager.getAsset("sandboxassets/True.sht/Sega");
        s2 = (Sprite) AssetManager.getAsset("sandboxassets/True.sht/Nintendo");
        sheet = (SpriteSheet) AssetManager.getAsset("internal/sheets/BlackGUI.sht");
        font = (Font) AssetManager.getAsset("internal/Test.fnt");
        label = new Label("U wanna...", font, Color.white);

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

        GUI.prepare(sheet, app);
        GUI.box(r5);
        if(GUI.buttonDown(r6, label))  GUI.label(r7, new Label("Cum in me", font, Color.red));
        GUI.unprepare(); // Will unprepare Core2D
    }
}