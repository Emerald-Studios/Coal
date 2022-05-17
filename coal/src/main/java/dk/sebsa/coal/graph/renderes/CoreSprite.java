package dk.sebsa.coal.graph.renderes;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.graph.GLSLShaderProgram;
import dk.sebsa.coal.graph.Mesh2D;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.math.Matrix4x4f;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * @author sebs
 * @since 1.0.0
 */
public class CoreSprite {
    private static final List<SpriteRenderer> rendererList = new ArrayList<>();
    private static final Map<GLSLShaderProgram, Map<Sprite, List<SpriteRenderer>>> renderMap = new HashMap<>();
    private static Mesh2D mainMesh;

    public static void renderME(SpriteRenderer e) {
        if(!renderMap.containsKey(e.shader)) renderMap.put(e.shader, new HashMap<>());
        if(!renderMap.get(e.shader).containsKey(e.sprite)) renderMap.get(e.shader).put(e.sprite, new ArrayList<>());
        renderMap.get(e.shader).get(e.sprite).add(e);
    }

    public static void render(Application app) {
        if(mainMesh == null) mainMesh = Mesh2D.getRenderMesh();
        glEnable(GL_DEPTH_TEST);
        float w = app.window.getWidth();
        float h = app.window.getHeight();
        float halfW = w * 0.5f;
        float halfH = h * 0.5f;

        Matrix4x4f projection = Matrix4x4f.ortho(-halfW, halfW, halfH, -halfH, -1, 1);
        mainMesh.bind();

        for(GLSLShaderProgram shader : renderMap.keySet()) {
            Map<Sprite, List<SpriteRenderer>> shaderMap = renderMap.get(shader);
            shader.bind();
            shader.setUniform("projection", projection);

            for(Sprite sprite : shaderMap.keySet()) {
                shader.setUniformAlt("matColor", sprite.getMaterial().getColor());
                sprite.getMaterial().getTexture().bind();

                for(SpriteRenderer renderer : shaderMap.get(sprite)) {
                    renderer.setUniforms();
                    GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 6);
                }

                sprite.getMaterial().getTexture().unbind();
            }
            shader.unbind();
        }

        mainMesh.unbind();
        renderMap.clear();
    }
}
