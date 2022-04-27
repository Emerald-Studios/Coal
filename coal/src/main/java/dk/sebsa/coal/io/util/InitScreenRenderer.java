package dk.sebsa.coal.io.util;

import dk.sebsa.coal.asset.AssetExitsException;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.graph.Texture;

import static org.lwjgl.opengl.GL11.*;

public class InitScreenRenderer {
    private static boolean init = false;

    private static void init() {
        init = true;
    }

    public static void render() {
        if(!init) init();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }
}
