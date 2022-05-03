package dk.sebsa.coal.graph.text;

import dk.sebsa.Coal;
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
    private void trace(Object o) { if(Coal.TRACE) log(o); }

    @Override @SneakyThrows
    protected void load() {
        List<String> raw = FileUtils.readAllLinesList(location.asStream());
        for(String line : raw) {
            if(line.startsWith("f")) awtFontName = line.split(":")[1];
            else if(line.startsWith("t")) awtFontType = Integer.parseInt(line.split(":")[1]);
            else if(line.startsWith("s")) awtFontSize = Integer.parseInt(line.split(":")[1]);
        } trace("Font read done. GenFont Begin");
        generateFont(new java.awt.Font(awtFontName, awtFontType, awtFontSize));
    }

    private void generateFont(java.awt.Font baseFont) {
        trace("Graph2D bufferimage gen");
        GraphicsConfiguration graphCon = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Graphics2D graphics = graphCon.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
        graphics.setFont(baseFont);

        fontMetrics = graphics.getFontMetrics();
        h = (float) (fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent());
        imageSize = new Vector2f(2048, 2048);
        bufferedImage = graphics.getDeviceConfiguration().createCompatibleImage((int) imageSize.x, (int) imageSize.y, Transparency.TRANSLUCENT);

        trace("Texture Gen");
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

    public static final char[] EXTENDED = { 0x00C7, 0x00FC, 0x00E9, 0x00E2,
            0x00E4, 0x00E0, 0x00E5, 0x00E7, 0x00EA, 0x00EB, 0x00E8, 0x00EF,
            0x00EE, 0x00EC, 0x00C4, 0x00C5, 0x00C9, 0x00E6, 0x00C6, 0x00F4,
            0x00F6, 0x00F2, 0x00FB, 0x00F9, 0x00FF, 0x00D6, 0x00DC, 0x00A2,
            0x00A3, 0x00A5, 0x20A7, 0x0192, 0x00E1, 0x00ED, 0x00F3, 0x00FA,
            0x00F1, 0x00D1, 0x00AA, 0x00BA, 0x00BF, 0x2310, 0x00AC, 0x00BD,
            0x00BC, 0x00A1, 0x00AB, 0x00BB, 0x2591, 0x2592, 0x2593, 0x2502,
            0x2524, 0x2561, 0x2562, 0x2556, 0x2555, 0x2563, 0x2551, 0x2557,
            0x255D, 0x255C, 0x255B, 0x2510, 0x2514, 0x2534, 0x252C, 0x251C,
            0x2500, 0x253C, 0x255E, 0x255F, 0x255A, 0x2554, 0x2569, 0x2566,
            0x2560, 0x2550, 0x256C, 0x2567, 0x2568, 0x2564, 0x2565, 0x2559,
            0x2558, 0x2552, 0x2553, 0x256B, 0x256A, 0x2518, 0x250C, 0x2588,
            0x2584, 0x258C, 0x2590, 0x2580, 0x03B1, 0x00DF, 0x0393, 0x03C0,
            0x03A3, 0x03C3, 0x00B5, 0x03C4, 0x03A6, 0x0398, 0x03A9, 0x03B4,
            0x221E, 0x03C6, 0x03B5, 0x2229, 0x2261, 0x00B1, 0x2265, 0x2264,
            0x2320, 0x2321, 0x00F7, 0x2248, 0x00B0, 0x2219, 0x00B7, 0x221A,
            0x207F, 0x00B2, 0x25A0, 0x00A0 };

    public static char getAscii(int code) {
        if (code >= 0x80 && code <= 0xFF) {
            return EXTENDED[code - 0x7F];
        }
        return (char) code;
    }

    private void drawCharaters(Graphics2D graphics2d) {
        int tempX = 0;
        int tempY = 0;

        for(i=32; i < 255; i++) {
            if(i==127) continue;

            char c = getAscii(i);
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
