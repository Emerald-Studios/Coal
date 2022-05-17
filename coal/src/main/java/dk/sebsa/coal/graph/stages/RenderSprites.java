package dk.sebsa.coal.graph.stages;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.graph.FBO;
import dk.sebsa.coal.graph.RenderStage;
import dk.sebsa.coal.graph.renderes.CoreSprite;

/**
 * @author sebs
 */
public class RenderSprites extends RenderStage {
    public RenderSprites(Application app) {
        super(app);
    }

    @Override
    public String getName() { return "Sprite Rendering"; }

    @Override
    protected void draw(FBO prevFBO) {
        renderPrevFBO(prevFBO);
        CoreSprite.render(app);
    }
}
