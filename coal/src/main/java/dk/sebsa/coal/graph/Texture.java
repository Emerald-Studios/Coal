package dk.sebsa.coal.graph;

import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import lombok.Getter;
import org.lwjgl.BufferUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

/**
 * @author sebs
 * @since 1.0.0
 */
public class Texture extends Asset {
    private int id;
    @Getter
    private float width, height;

    public Texture(AssetLocation location) {
        super(location);
    }

    public Texture(TextureInfo ti) {
        super(AssetLocation.none);
        load(ti);
    }

    @Override
    public void load() {
        load(loadTexture());
    }

    public Texture load(TextureInfo ti) {
        this.width = ti.width;
        this.height = ti.height;
        this.id = ti.id;
        return this;
    }

    private TextureInfo loadTexture() {
        ByteBuffer data;

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

        InputStream is = location.asStream();

        // Load texture from stream
        byte[] bytes = new byte[8000];
        int curByte;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            while((curByte = is.read(bytes)) != -1) { bos.write(bytes, 0, curByte);}
            is.close();
        } catch (IOException e) { e.printStackTrace(); }

        bytes = bos.toByteArray();
        ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
        buffer.put(bytes).flip();
        data = stbi_load_from_memory(buffer, widthBuffer, heightBuffer, channelsBuffer, 4);

        // Create a new OpenGL texture
        int textureId = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Upload the texture data
        int width = widthBuffer.get();
        int height = heightBuffer.get();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, data);
        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);

        assert data != null;
        stbi_image_free(data);

        return new TextureInfo(width, height, textureId);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    @Override
    public void destroy() {
        glDeleteTextures(id);
    }

    public void unbind() { glBindTexture(GL_TEXTURE_2D, 0); }

    public static class TextureInfo {
        public int width;
        public int height;
        public int id;

        public TextureInfo(int w, int h, int i) {
            this.width = w;
            this.height = h;
            this.id = i;
        }
    }
}
