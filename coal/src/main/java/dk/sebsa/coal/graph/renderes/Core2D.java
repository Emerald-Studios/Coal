package dk.sebsa.coal.graph.renderes;

import dk.sebsa.Coal;
import dk.sebsa.coal.asset.AssetManager;
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
    private static GLFWWindow window;
    public static GLSLShaderProgram defaultShader;
    private static Mesh2D guiMesh;
    private static Matrix4x4f ortho;
    private static Color currentColor;
    @Getter private static boolean prepared = false;
    private static Texture noTexture;

    private static void log(Object o) { Coal.logger.log(o); }
    private static void trace() { if(Coal.TRACE) Coal.logger.log("Shader Uniforms"); }

    public static void init(GLFWWindow w, GLSLShaderProgram s) {
        log("Initializing Core2D");
        window = w;
        defaultShader = s;

        guiMesh = Mesh2D.getQuad();
        trace();
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
        noTexture = (Texture) AssetManager.getAsset("internal/textures/NoTexture.png");
        prepared = true;
    }

    public static void unprepare() {
        // Enable 3d
        glEnable(GL_DEPTH_TEST);

        defaultShader.unbind();
        guiMesh.unbind();
        prepared = false;
    }

    private static final Rect fullUV = new Rect(0,0,1,1);

    public static void drawTextureWithTextCoords(Material mat, Rect drawRect) { drawTextureWithTextCoords(mat, drawRect, fullUV, guiMesh, defaultShader); }
    public static void drawTextureWithTextCoords(Material mat, Rect drawRect, Rect uvRect) { drawTextureWithTextCoords(mat, drawRect, uvRect, guiMesh, defaultShader); }


    private static final Rect u = new Rect(0,0,0,0);
    private static final Rect r = new Rect(0,0,0,0);
    private static final Rect r2 = new Rect(0,0,0,0);
    public static void drawTextureWithTextCoords(Material mat, Rect drawRect, Rect uvRect, Mesh2D mesh, GLSLShaderProgram shader) {
        window.rect.getIntersection(r2.set(drawRect.x, drawRect.y, drawRect.width, drawRect.height), r);

        // uvreact
        float x = uvRect.x + (((r.x - drawRect.x) / drawRect.width) * uvRect.width);
        float y = uvRect.y + (((r.y - drawRect.y) / drawRect.height) * uvRect.height);
        u.set(x, y, (r.width / drawRect.width) * uvRect.width, (r.height / drawRect.height) * uvRect.height);

        // Draw
        changeColor(mat.getColor());
        if(mat.getTexture() != null) mat.getTexture().bind();
        else { noTexture.bind(); u.set(0,0,1,1); }
        shader.setUniform("useColor", mat.isTextured() ? 0 : 1);
        shader.setUniform("offset", u.x, u.y, u.width, u.height);
        shader.setUniform("pixelScale", r.width, r.height);
        shader.setUniform("screenPos", r.x, r.y);

        GL20.glDrawArrays(GL30.GL_TRIANGLES, 0, 6);
        if(mat.getTexture() != null) mat.getTexture().bind();
        else noTexture.bind();
    }

    private static final Rect rect1 = new Rect();   // This one is used for multiple things
    private static final Rect rect2 = new Rect();   // This one is used for multiple things
    private static final Rect tr = new Rect();
    private static final Rect tru = new Rect();
    private static final Rect ti = new Rect();
    private static final Rect tu = new Rect();
    private static final Rect bl = new Rect();
    private static final Rect blu = new Rect();
    private static final Rect l = new Rect();
    private static final Rect lu = new Rect();
    public static void drawSprite(Rect r, Sprite e) {
        //Cache a short variable for the texture, just so we only have to type a character anytime we use it
        Rect uv = e.getUV();

        //Get the top left corner of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(r.x, r.y, e.getPadding().x, e.getPadding().y);   // TL
        rect2.set(uv.x, uv.y, e.getPaddingUV().x, e.getPaddingUV().y); // TLU
        drawTextureWithTextCoords(e.getMaterial(), rect1, rect2);

        //Get the top right corner of the box using corresponding padding values and draw it using a texture drawing method
        tr.set((r.x + r.width) - e.getPadding().width, r.y, e.getPadding().width, e.getPadding().y);    // TR
        tru.set((uv.x + uv.width) - e.getPaddingUV().width, uv.y, e.getPaddingUV().width, e.getPaddingUV().y); // TRU
        drawTextureWithTextCoords(e.getMaterial(), tr, tru);

        //Get the bottom left corner of the box using corresponding padding values and draw it using a texture drawing method
        bl.set(r.x, (r.y + r.height) - e.getPadding().height, e.getPadding().x, e.getPadding().height); // BL
        blu.set(uv.x, (uv.y + uv.height) - e.getPaddingUV().height, e.getPaddingUV().x, e.getPaddingUV().height); //BLU
        drawTextureWithTextCoords(e.getMaterial(), bl, blu);

        //Get the bottom right corner of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(tr.x, bl.y, e.getPadding().width, e.getPadding().height); // BR
        rect2.set(tru.x, blu.y, e.getPaddingUV().width, e.getPaddingUV().height); // BRU
        drawTextureWithTextCoords(e.getMaterial(), rect1, rect2);

        //Get the left side of the box using corresponding padding values and draw it using a texture drawing method
        l.set(r.x, r.y + e.getPadding().y, e.getPadding().x, r.height - (e.getPadding().y + e.getPadding().height)); // L
        lu.set(uv.x, uv.y + e.getPaddingUV().y, e.getPaddingUV().x, uv.height - (e.getPaddingUV().y + e.getPaddingUV().height)); //LU
        drawTextureWithTextCoords(e.getMaterial(), l, lu);

        //Get the right side of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(tr.x, r.y + e.getPadding().y, e.getPadding().width, l.height); // RI
        rect2.set(tru.x, lu.y, e.getPaddingUV().width, lu.height); // RU
        drawTextureWithTextCoords(e.getMaterial(), rect1, rect2);

        //Get the top of the box using corresponding padding values and draw it using a texture drawing method
        ti.set(r.x + e.getPadding().x, r.y, r.width - (e.getPadding().x + e.getPadding().width), e.getPadding().y); // TI
        tu.set(uv.x + e.getPaddingUV().x, uv.y, uv.width - (e.getPaddingUV().x + e.getPaddingUV().width), e.getPaddingUV().y); // TU
        drawTextureWithTextCoords(e.getMaterial(), ti, tu);

        //Get the bottom of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(ti.x, bl.y, ti.width, e.getPadding().height); // B
        rect2.set(tu.x, blu.y, tu.width, e.getPaddingUV().height); // BU
        drawTextureWithTextCoords(e.getMaterial(), rect1, rect2);

        //Get the center of the box using corresponding padding values and draw it using a texture drawing method
        rect1.set(ti.x, l.y, ti.width, l.height); // C
        rect2.set(tu.x, lu.y, tu.width, lu.height); // CU
        drawTextureWithTextCoords(e.getMaterial(), rect1, rect2);
    }
}
