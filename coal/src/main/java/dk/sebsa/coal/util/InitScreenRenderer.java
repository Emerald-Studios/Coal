package dk.sebsa.coal.util;

import dk.sebsa.coal.asset.AssetExitsException;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.graph.Material;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.Texture;
import dk.sebsa.coal.graph.renderes.Core2D;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author sebs
 * @since 1.0.0
 */
public class InitScreenRenderer {
    private static boolean init = false;
    private static Material mat;

    private static void init() {
        init = true;
        Texture texture;
        try { texture = (Texture) new Texture(new AssetLocation(AssetLocationType.Jar, "/coal/internal/textures/Loading.png")).loadAsset(); }
        catch (AssetExitsException e) { texture = (Texture) AssetManager.getAsset("internal/textures/Loading.png"); }
        mat = new Material(texture);
    }

    public static void render(Rect r) {
        if(!init) init();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        Core2D.drawTextureWithTextCoords(mat, r);
    }
}
