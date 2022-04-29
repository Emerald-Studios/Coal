package dk.sebsa.coal.graph.renderes;

import dk.sebsa.Coal;
import dk.sebsa.coal.graph.*;
import dk.sebsa.coal.io.GLFWWindow;
import dk.sebsa.coal.math.Color;
import dk.sebsa.coal.math.Matrix4x4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author sebs
 * @since 1.0.0
 */
public class Core2D {
    // Constants
    private static final Color white = Color.white;
    private static final Rect ZERO_RECT = new Rect();
    private static GLFWWindow window;
    private static GLSLShaderProgram defaultShader;
    private static Mesh2D guiMesh;
    private static Matrix4x4f ortho;
    private static Color currentColor;

    private static void log(Object o) { Coal.logger.log(o); }

    public static void init(GLFWWindow w, GLSLShaderProgram s) {
        log("Initializing Core2D");
        window = w;
        defaultShader = s;

        guiMesh = Mesh2D.getQuad();
        try {
            defaultShader.createUniform("projection");
            defaultShader.createUniform("offset");
            defaultShader.createUniform("pixelScale");
            defaultShader.createUniform("screenPos");
            defaultShader.createUniform("color");
            defaultShader.createUniform("useColor");
        } catch (Exception e) { e.printStackTrace(); }
        log("Core2D Done");
    }

    private static void changeColor(Color c) {
        if(c==currentColor) return;
        defaultShader.setUniformAlt("color", c);
        currentColor = c;
    }

    public static void prepare() {
        // Disable 3d
        glDisable(GL_DEPTH_TEST);

        if(window.isDirty()) ortho = Matrix4x4f.ortho(0, window.getWidth(), window.getHeight(), 0, -1, 1);

        // Render preparation
        defaultShader.bind();
        defaultShader.setUniform("projection", ortho);
        changeColor(Color.white);
        guiMesh.bind();
    }

    public static void unprepare() {
        // Enable 3d
        glEnable(GL_DEPTH_TEST);

        defaultShader.unbind();
        guiMesh.unbind();
    }

    public static void drawTextureWithTextCoords(Texture tex, Rect drawRect) { drawTextureWithTextCoords(tex, drawRect, new Rect(0,0,1,1), guiMesh, defaultShader); }
    public static void drawTextureWithTextCoords(Texture tex, Rect drawRect, Rect uvRect) { drawTextureWithTextCoords(tex, drawRect, uvRect, guiMesh, defaultShader); }
    public static void drawTextureWithTextCoords(Material mat, Rect drawRect) { changeColor(mat.getColor()); drawTextureWithTextCoords(mat.getTexture(), drawRect, new Rect(0,0,1,1), guiMesh, defaultShader); }
    public static void drawTextureWithTextCoords(Material mat, Rect drawRect, Rect uvRect) { changeColor(mat.getColor()); drawTextureWithTextCoords(mat.getTexture(), drawRect, uvRect, guiMesh, defaultShader); }

    private static final Rect u = new Rect(0,0,0,0);
    private static final Rect r = new Rect(0,0,0,0);
    private static final Rect r2 = new Rect(0,0,0,0);
    public static void drawTextureWithTextCoords(Texture tex, Rect drawRect, Rect uvRect, Mesh2D mesh, GLSLShaderProgram shader) {
        window.rect.getIntersection(r2.set(drawRect.x, drawRect.y, drawRect.width, drawRect.height), r);

        // uvreact
        float x = uvRect.x + (((r.x - drawRect.x) / drawRect.width) * uvRect.width);
        float y = uvRect.y + (((r.y - drawRect.y) / drawRect.height) * uvRect.height);
        u.set(x, y, (r.width / drawRect.width) * uvRect.width, (r.height / drawRect.height) * uvRect.height);

        // Draw
        if(tex != null) tex.bind();
        shader.setUniform("useColor", tex == null ? 1 : 0);
        shader.setUniform("offset", u.x, u.y, u.width, u.height);
        shader.setUniform("pixelScale", r.width, r.height);
        shader.setUniform("screenPos", r.x, r.y);

        GL20.glDrawArrays(GL30.GL_TRIANGLES, 0, 6);
        if(tex != null) tex.unbind();
    }
}
