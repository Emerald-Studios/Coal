package dk.sebsa.coal.graph;


import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.graph.renderes.Core2D;
import dk.sebsa.coal.trash.Trash;
import dk.sebsa.coal.trash.TrashCollector;
import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author sebs
 * @since 1.0.0
 */
public class FBO extends Trash {
    private final int frameBufferID;
    private final int depthBufferID;
    private final Texture texture;
    @Getter private final Material mat;
    public final int width;
    public final int height;

    private static final List<FBO> fbos = new ArrayList<>();
    private void trace(Object o) { if(Coal.TRACE) Coal.logger.log(o);}

    public FBO(int w, int h) {
        this.width = w;
        this.height = h;
        frameBufferID = createFrameBuffer();
        depthBufferID = createDepthBufferAttachment();

        int textureID = createTextureAttachment();
        texture = new Texture(new Texture.TextureInfo(width, height, textureID));
        mat = new Material(texture);
        unBind();

        fbos.add(this);
    }

    public void bindFrameBuffer() {
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBufferID);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBufferID);
        glViewport(0,0,width,height);
    }

    public void unBind() {
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        glViewport(0,0,Coal.instance.getApplication().window.getWidth(),Coal.instance.getApplication().window.getHeight());
    }

    public void dispose() { // Will schedule the TC to destroy this
        if(Coal.getCapabilities().tcFBOClean) TrashCollector.trash(this);
    }

    private int createTextureAttachment() {
        trace("Gen Texture Attachment");
        int texture = GL11.glGenTextures();

        GL11.glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);

        return texture;
    }

    private int createDepthBufferAttachment() {
        trace("Gen DepthBuffer Attachment");
        int buffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, buffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,buffer);
        return buffer;
    }

    private int createFrameBuffer() {
        trace("Creating FrameBuffer");
        int buffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        return buffer;
    }

    @Override
    public void destroy() {
        GL30.glDeleteFramebuffers(frameBufferID);
        texture.destroy();

        fbos.remove(this);
    }

    public static void renderFBO(Application app, FBO fbo, Rect r, Rect r2) {
        if(fbo == null) return;
        Core2D.prepare();
        Core2D.drawTextureWithTextCoords(fbo.getMat(), r, r2);
        Core2D.unprepare();
    }
}

