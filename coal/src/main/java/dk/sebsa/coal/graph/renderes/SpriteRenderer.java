package dk.sebsa.coal.graph.renderes;

import dk.sebsa.coal.asset.AssetExitsException;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.ecs.Component;
import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.graph.GLSLShaderProgram;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.math.Vector2f;

/**
 * @author sebs
 * @since 1.0.0
 */
public class SpriteRenderer extends Component {
    public GLSLShaderProgram shader;
    public Sprite sprite;
    public Vector2f anchor = new Vector2f(0.5f, 0.5f);
    private static GLSLShaderProgram defaultShader;

    public SpriteRenderer(Sprite sprite) {
        if(defaultShader == null) throw new RuntimeException("SpriteRenderer not properly inited");

        this.sprite = sprite;
        this.shader = defaultShader;
    }

    public static void initDefaultShader() {
        if(defaultShader == null) {
            GLSLShaderProgram shader;
            try { shader = (GLSLShaderProgram) new GLSLShaderProgram(new AssetLocation(AssetLocationType.Jar, "/coal/internal/shaders/CoalSpriteDefault.glsl")).loadAsset(); }
            catch (AssetExitsException e) { shader = (GLSLShaderProgram) AssetManager.getAsset("internal/shaders/CoalSpriteDefault.glsl"); }
            defaultShader = shader;
            try {
                defaultShader.createUniform("anchor");
                defaultShader.createUniform("transformMatrix");
                //defaultShader.createUniform("screenPos");
                defaultShader.createUniform("pixelScale");
                defaultShader.createUniform("objectScale");
                defaultShader.createUniform("anchor");
                defaultShader.createUniform("offset");
                defaultShader.createUniform("projection");
                defaultShader.createUniform("matColor");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setUniforms() {
        shader.setUniform("anchor", anchor);

        shader.setUniform("transformMatrix", entity.transform.getMatrix2D());

        //shader.setUniform("screenPos",entity.transform.getPosition().x, entity.transform.getPosition().y);

        shader.setUniform("pixelScale", sprite.getOffset().width, sprite.getOffset().height);
        shader.setUniform("objectScale", 1,1);

        Rect uvRect = sprite.getUV();
        shader.setUniform("offset", uvRect.x, uvRect.y, uvRect.width, uvRect.height);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void update(GLFWInput input) {
        CoreSprite.renderME(this);
    }

    @Override
    protected void lateUpdate(GLFWInput input) {

    }
}
