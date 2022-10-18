package dk.sebsa.coal.graph.renderes;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.graph.text.Glyph;
import dk.sebsa.coal.graph.text.Label;

import java.util.Map;

/**
 * @author sebs
 * @since 1.0.0
 */
public class GUI {
    public static Sprite spriteBox;
    public static Sprite spriteButton;
    public static Sprite spriteButtonHover;
    private static Application app;

    public static void prepare(SpriteSheet sheet, Application application) {
        if(!Core2D.isPrepared()) Core2D.prepare();
        app = application;

        genSprites(sheet);
    }

    public static void genSprites(SpriteSheet sheet) {
        spriteBox = sheet.getSprite("Box");
        spriteButton = sheet.getSprite("Button");
        spriteButtonHover = sheet.getSprite("ButtonHover");
    }

    public static void box(Rect rect) {
        Core2D.drawSprite(rect, spriteBox);
    }
    public static void box(Rect rect, Sprite sprite) { if(sprite != null) Core2D.drawSprite(rect, sprite); }

    public static void button(Rect r, Label label, Sprite sprite, Sprite hoverSprite) {
        box(r, r.contains(app.input.getMousePos()) ? hoverSprite : sprite);
        if(label != null) label(r, label);
    }

    private static final Rect offsetRect = new Rect();

    public static void button(Rect r, Label label, Sprite sprite, Sprite hoverSprite, float textOffset) {
        offsetRect.set(r); offsetRect.add(textOffset,0,0,0);
        box(r, r.contains(app.input.getMousePos()) ? hoverSprite : sprite);
        label(offsetRect, label);
    }

    private static final Rect r1 = new Rect();
    private static final Rect r2 = new Rect();
    public static void label(Rect r, Label label) {
        Map<Character, Glyph> chars = label.getFont().getChars();
        char[] c = label.getCharArray();
        float tempX = r.x;

        for (char value : c) {
            Glyph glyph = chars.get(value);

            Core2D.drawTextureWithTextCoords(label.getMaterial(), r1.set(tempX, r.y, glyph.scale().x, glyph.scale().y), r2.set(glyph.position().x, glyph.position().y, glyph.size().x, glyph.size().y));

            tempX += glyph.scale().x;
        }
    }

    public static void unprepare() {
        if(Core2D.isPrepared()) Core2D.unprepare();
    }
}
