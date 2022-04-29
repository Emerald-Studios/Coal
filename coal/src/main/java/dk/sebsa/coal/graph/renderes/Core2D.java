package dk.sebsa.coal.graph.renderes;

import dk.sebsa.Coal;
import dk.sebsa.coal.graph.*;
import dk.sebsa.coal.io.GLFWWindow;
import dk.sebsa.coal.math.Color;
import dk.sebsa.coal.math.Matrix4x4f;
import lombok.Getter;
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
    public static GLSLShaderProgram defaultShader;
    private static Mesh2D guiMesh;
    private static Matrix4x4f ortho;
    private static Color currentColor;
    @Getter private static boolean prepared = false;

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
        prepared = true;
    }

    public static void unprepare() {
        // Enable 3d
        glEnable(GL_DEPTH_TEST);

        defaultShader.unbind();
        guiMesh.unbind();
        prepared = false;
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

    public static void drawSprite(Rect r, Sprite e) {
        changeColor(e.getMaterial().getColor());
        //Cache a short variable for the texture, just so we only have to type a character anytime we use it
        Texture t = e.getMaterial().getTexture();
        Rect uv = e.getUV();

        //Get the top left corner of the box using corresponding padding values and draw it using a texture drawing method
        Rect tl = new Rect(r.x, r.y, e.getPadding().x, e.getPadding().y);
        Rect tlu = new Rect(uv.x, uv.y, e.getPaddingUV().x, e.getPaddingUV().y);
        drawTextureWithTextCoords(t, tl, tlu);

        //Get the top right corner of the box using corresponding padding values and draw it using a texture drawing method
        Rect tr = new Rect((r.x + r.width) - e.getPadding().width, r.y, e.getPadding().width, e.getPadding().y);
        Rect tru = new Rect((uv.x + uv.width) - e.getPaddingUV().width, uv.y, e.getPaddingUV().width, e.getPaddingUV().y);
        drawTextureWithTextCoords(t, tr, tru);

        //Get the bottom left corner of the box using corresponding padding values and draw it using a texture drawing method
        Rect bl = new Rect(r.x, (r.y + r.height) - e.getPadding().height, e.getPadding().x, e.getPadding().height);
        Rect blu = new Rect(uv.x, (uv.y + uv.height) - e.getPaddingUV().height, e.getPaddingUV().x, e.getPaddingUV().height);
        drawTextureWithTextCoords(t, bl, blu);

        //Get the bottom right corner of the box using corresponding padding values and draw it using a texture drawing method
        Rect br = new Rect(tr.x, bl.y, e.getPadding().width, e.getPadding().height);
        Rect bru = new Rect(tru.x, blu.y, e.getPaddingUV().width, e.getPaddingUV().height);
        drawTextureWithTextCoords(t, br, bru);

        //Get the left side of the box using corresponding padding values and draw it using a texture drawing method
        Rect l = new Rect(r.x, r.y + e.getPadding().y, e.getPadding().x, r.height - (e.getPadding().y + e.getPadding().height));
        Rect lu = new Rect(uv.x, uv.y + e.getPaddingUV().y, e.getPaddingUV().x, uv.height - (e.getPaddingUV().y + e.getPaddingUV().height));
        drawTextureWithTextCoords(t, l, lu);

        //Get the right side of the box using corresponding padding values and draw it using a texture drawing method
        Rect ri = new Rect(tr.x, r.y + e.getPadding().y, e.getPadding().width, l.height);
        Rect ru = new Rect(tru.x, lu.y, e.getPaddingUV().width, lu.height);
        drawTextureWithTextCoords(t, ri, ru);

        //Get the top of the box using corresponding padding values and draw it using a texture drawing method
        Rect ti = new Rect(r.x + e.getPadding().x, r.y, r.width - (e.getPadding().x + e.getPadding().width), e.getPadding().y);
        Rect tu = new Rect(uv.x + e.getPaddingUV().x, uv.y, uv.width - (e.getPaddingUV().x + e.getPaddingUV().width), e.getPaddingUV().y);
        drawTextureWithTextCoords(t, ti, tu);

        //Get the bottom of the box using corresponding padding values and draw it using a texture drawing method
        Rect b = new Rect(ti.x, bl.y, ti.width, e.getPadding().height);
        Rect bu = new Rect(tu.x, blu.y, tu.width, e.getPaddingUV().height);
        drawTextureWithTextCoords(t, b, bu);

        //Get the center of the box using corresponding padding values and draw it using a texture drawing method
        Rect c = new Rect(ti.x, l.y, ti.width, l.height);
        Rect cu = new Rect(tu.x, lu.y, tu.width, lu.height);
        drawTextureWithTextCoords(t, c, cu);
    }
}
