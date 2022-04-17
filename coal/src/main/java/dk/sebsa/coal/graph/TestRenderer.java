package dk.sebsa.coal.graph;

import dk.sebsa.coal.asset.AssetManager;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class TestRenderer {
    private static Mesh mesh;
    private static GLSLShaderProgram shader;

    public static void render(Mesh mesh) {
        if(shader == null) shader = (GLSLShaderProgram) AssetManager.getAsset("/coal/internal/shaders/Coal3dDefault.glsl");
        shader.bind();

        // Draw the mesh
        glBindVertexArray(mesh.getVaoId());
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glBindVertexArray(0);

        shader.unbind();
    }
}
