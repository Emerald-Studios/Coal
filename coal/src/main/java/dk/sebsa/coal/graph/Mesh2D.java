package dk.sebsa.coal.graph;

import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetExitsException;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.enums.AssetLocationType;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

/**
 * @author sebs
 * @since 1.0.0
 */
public class Mesh2D extends Asset {
    private int v_id;
    private int u_id;
    private int vao;

    private final float[] vertices, uvs;

    // Static Stuff
    private static final float[] square = new float[] {
            0, 1, 1, 1, 1, 0,
            1, 0, 0, 0, 0, 1
    };

    public static Mesh2D getQuad() {
        try {
            return (Mesh2D) new Mesh2D("coal/internal/Quad2D", square, square).loadAsset();
        } catch (AssetExitsException e) { return (Mesh2D) AssetManager.getAsset("coal/internal/Quad2D"); }
    }

    // Main class
    public Mesh2D(String name, float[] vertices, float[] uvs) {
        super(new AssetLocation(AssetLocationType.Code, name));
        this.vertices = vertices;
        this.uvs = uvs;
    }

    @Override
    protected void load() {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        v_id = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, v_id);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, createBuffer(vertices), GL30.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, vertices.length/3, GL30.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        u_id = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, u_id);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, createBuffer(uvs), GL30.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void bind() {
        GL30.glBindVertexArray(vao);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, v_id);
        GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 0, 0);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, u_id);
        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 0, 0);
    }

    public void unbind() {
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    @Override
    public void destroy() {
        GL30.glDeleteVertexArrays(vao);
        GL15.glDeleteBuffers(v_id);
        GL15.glDeleteBuffers(u_id);
    }
}
