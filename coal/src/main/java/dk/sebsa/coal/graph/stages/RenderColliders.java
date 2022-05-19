package dk.sebsa.coal.graph.stages;

import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.ecs.collision.BoxCollider2D;
import dk.sebsa.coal.ecs.collision.Collider2D;
import dk.sebsa.coal.graph.*;
import dk.sebsa.coal.math.Matrix4x4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL42.GL_LINES;

/**
 * @author sebs
 */
public class RenderColliders extends RenderStage {
    public RenderColliders(Application app) {
        super(app);
    }

    @Override
    public String getName() { return "RenderColliders"; }

    private GLSLShaderProgram shader;
    private VAO vao1;
    private Matrix4x4f projection;
    private final float[] testPos = {
            0.0f, 0.0f,
            1,    0.0f,
            0.0f, 0.0f,
            -1,    0.0f,
    };

    private void setProjection() {
        float w = app.window.getWidth();
        float h = app.window.getHeight();
        float halfW = w * 0.5f;
        float halfH = h * 0.5f;
        projection = Matrix4x4f.ortho(-halfW, halfW, halfH, -halfH, -1, 1);
    }

    private final List<Float> pos = new ArrayList<>();
    @Override
    protected void draw(FBO prevFBO) {
        renderPrevFBO(prevFBO);
        if(app.window.isDirty()) setProjection();

        pos.clear();
        for (Collider2D collider : Collider2D.getCOLLIDERS()) {
            if (collider instanceof BoxCollider2D) {
                Rect r = ((BoxCollider2D) collider).getWorldPositionRect();

                // C1 to C2
                pos.add(r.x); pos.add(r.y);
                pos.add(r.x+r.width); pos.add(r.y);

                // C2 to C3
                pos.add(r.x+r.width); pos.add(r.y);
                pos.add(r.x+r.width); pos.add(r.y-r.height);

                // C3 to C4
                pos.add(r.x+r.width); pos.add(r.y-r.height);
                pos.add(r.x); pos.add(r.y-r.height);

                // C4 to C1
                pos.add(r.x); pos.add(r.y-r.height);
                pos.add(r.x); pos.add(r.y);

                // C1 C2
                // C4 C3
            }
        }

        // List to Array
        float[] vertices = new float[pos.size()];
        int i = 0;
        for (Float f : pos) {
            vertices[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        vao1.put(vertices);

        shader.bind();
        shader.setUniform("projection", projection);
        shader.setUniform("mode", 2);

        vao1.draw(GL_LINES);
        shader.unbind();
    }

    @Override
    protected void onEnable() {
        if(!Coal.DEBUG) setEnabled(false);

        vao1 = new VAO(testPos, 2);
        setProjection();

        if (shader != null) return;
        shader = (GLSLShaderProgram) AssetManager.getAsset("internal/shaders/CoalBlankShader.glsl");;
        try {
            shader.createUniform("projection");
            shader.createUniform("mode");
            shader.createUniform("pixelScale");
        } catch (Exception e) { Coal.logger.log("onEnable failed to create uniforms", getName()); }
    }

    @Override
    protected void onDisable() {
        if(vao1 != null) vao1.trash();
    }
}
