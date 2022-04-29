package dk.sebsa.coal.util;

import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.graph.Mesh;

/**
 * @author sebs
 * @since 1.0.0
 */
public class DebugRenderingUtils {
    public static final Mesh SQUARE_MESH = new Mesh(AssetLocation.none, new float[]{
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f,
    }, new int[]{
            0, 1, 3, 3, 1, 2
    });

    public static final float[] VERTICIES_TRIANGLE = new float[]{
            0.0f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };
}
