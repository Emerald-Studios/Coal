package dk.sebsa.coal.graph.text;

import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.graph.Texture;
import dk.sebsa.coal.math.Vector2f;
import dk.sebsa.coal.util.FileUtils;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author sebs
 */
public class Font extends Asset {
    private BufferedImage bufferedImage;
    private Vector2f imageSize;
    private FontMetrics fontMetrics;
    private int i;
    private Texture texture;
    private float h;

    private String awtFontName;
    private int awtFontType, awtFontSize;

    private final Map<Character, Glyph> chars = new HashMap<>();

    public Font(AssetLocation assetLocation) {
        super(assetLocation);
    }

    @Override @SneakyThrows
    protected void load() {
        List<String> raw = FileUtils.readAllLinesList(location.asStream());
        for(String line : raw) {
            if(line.startsWith("f")) awtFontName = line.split(":")[1];
            else if(line.startsWith("t")) awtFontType = Integer.parseInt(line.split(":")[1]);
            else if(line.startsWith("s")) awtFontSize = Integer.parseInt(line.split(":")[1]);
        }
        generateFont(new java.awt.Font(awtFontName, awtFontType, awtFontSize));
    }

    private void generateFont(java.awt.Font baseFont) {
        GraphicsConfiguration graphCon = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Graphics2D graphics = graphCon.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
        graphics.setFont(baseFont);

        fontMetrics = graphics.getFontMetrics();
        h = (float) (fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent());
        imageSize = new Vector2f(2048, 2048);
        bufferedImage = graphics.getDeviceConfiguration().createCompatibleImage((int) imageSize.x, (int) imageSize.y, Transparency.TRANSLUCENT);

        int fontID = glGenTextures();
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, fontID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (int)imageSize.x, (int)imageSize.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, generateImage(baseFont));

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        texture = new Texture(new Texture.TextureInfo((int)imageSize.x, (int)imageSize.y, fontID));
    }

    private ByteBuffer generateImage(java.awt.Font baseFont) {
        Graphics2D graphics2d = (Graphics2D) bufferedImage.getGraphics();
        graphics2d.setFont(baseFont);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawCharaters(graphics2d);
        return createBuffer();
    }

    private ByteBuffer createBuffer() {
        int w = (int)imageSize.x;
        int h = (int)imageSize.y;
        int[] pixels = new int[w*h];

        bufferedImage.getRGB(0, 0, w, h, pixels, 0, w);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(w * h * 4);

        for(i = 0; i < pixels.length; i++) {
            byteBuffer.put((byte) ((pixels[i] >> 16) & 0xFF)); 	// Red
            byteBuffer.put((byte) ((pixels[i] >> 8) & 0xFF)); 	// Green
            byteBuffer.put((byte) (pixels[i] >> 0xFF)); 		// Blue
            byteBuffer.put((byte) ((pixels[i] >> 24) & 0xFF)); 	// Alpha
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    private void drawCharaters(Graphics2D graphics2d) {
        int tempX = 0;
        int tempY = 0;

        for(i=32; i < 256; i++) {
            if(i==127) continue;

            char c = (char) i;
            float charWidth = fontMetrics.charWidth(c);

            float advance = charWidth + 8;

            if(tempX + advance > imageSize.x) {
                tempX = 0;
                tempY += 1;
            }
            chars.put(c, new Glyph(new Vector2f(tempX / imageSize.x, (tempY * h) / imageSize.y), new Vector2f(charWidth / imageSize.x, h/imageSize.y), new Vector2f(charWidth, h)));
            graphics2d.drawString(String.valueOf(c), tempX, fontMetrics.getMaxAscent() + (h* tempY));
            tempX += advance;
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public Map<Character, Glyph> getChars() {
        return chars;
    }

    public int getStringWidth(String s) {
        return fontMetrics.stringWidth(s);
    }

    public float getFontHeight() { return h; }

    public final String getName() { return name; }

    @Override
    public void destroy() {

    }
}
