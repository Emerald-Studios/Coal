package dk.sebsa.coal.graph;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.graph.renderes.Render2D;

import static org.lwjgl.opengl.GL11.*;

public abstract class RenderStage {
    public abstract String getName();
    private boolean init = false; // Init is done later, so it has opengl context
    private FBO fbo;
    protected final Application app;

    public RenderStage(Application app) {
        this.app = app;
    }

    public void init() {
        updateFBO();
        init = true;
    }

    private void updateFBO() {
        if(fbo != null) fbo.destroy();
        fbo = new FBO(app.window.getWidth(), app.window.getHeight());
        fbo.bindFrameBuffer();
        glEnable(GL_DEPTH_TEST);
        fbo.unBind();
    }

    protected FBO render(FBO prevFBO) {
        if(!init) init();
        fbo.bindFrameBuffer();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        draw(prevFBO);

        fbo.unBind();
        return fbo;
    }

    public static final Rect r = new Rect(0, 1, 1, -1);
    protected void renderPrevFBO(FBO prevFBO) {
        FBO.renderFBO(app, prevFBO, r);
        fbo.bindFrameBuffer();
    }

    protected abstract void draw(FBO prevFBO);
}
