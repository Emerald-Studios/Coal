package dk.sebsa.coal.graph;

import dk.sebsa.coal.Application;
import lombok.Getter;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author sebs
 * @since 1.0.0
 */
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
        checkEnabled();
    }

    private void updateFBO() {
        if(fbo != null) fbo.dispose();
        fbo = new FBO(app.window.getWidth(), app.window.getHeight());
        fbo.bindFrameBuffer();
        glEnable(GL_DEPTH_TEST);
        fbo.unBind();
    }

    protected FBO render(FBO prevFBO) {
        if(!init || app.window.isDirty()) init();
        checkEnabled();
        if(!enabled) return prevFBO;
        fbo.bindFrameBuffer();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        draw(prevFBO);

        fbo.unBind();
        return fbo;
    }

    public static final Rect r = new Rect(0, 1, 1, -1);
    public final Rect fboRect = new Rect();
    //public static final Rect r = new Rect(0, 0, 1, -1);
    protected void renderPrevFBO(FBO prevFBO) {
        if(prevFBO == null) return;
        FBO.renderFBO(app, prevFBO, fboRect.set(0,0,fbo.width, fbo.height), r);
        fbo.bindFrameBuffer();
    }

    protected abstract void draw(FBO prevFBO);

    @Getter private boolean enabled = true;
    private boolean upToDate = false; // Enabling should be done on rendering thread not update thread
    public void setEnabled(boolean e) { enabled = e; upToDate = false; }
    private void checkEnabled() {
        if(upToDate) return;
        if(enabled) onEnable(); else onDisable();
        upToDate = true;
    }
    protected abstract void onEnable();
    protected abstract void onDisable();
}
