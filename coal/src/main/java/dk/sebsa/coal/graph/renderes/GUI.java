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
    private static Sprite spriteBox;
    private static Sprite spriteButton;
    private static Sprite spriteButtonHover;
    private static Application app;

    public static void prepare(SpriteSheet sheet, Application application) {
        if(!Core2D.isPrepared()) Core2D.prepare();
        spriteBox = sheet.getSprite("Box");
        spriteButton = sheet.getSprite("Button");
        spriteButtonHover = sheet.getSprite("ButtonHover");
        app = application;
    }

    public static void box(Rect rect) {
        Core2D.drawSprite(rect, spriteBox);
    }
    public static void box(Rect rect, Sprite sprite) { Core2D.drawSprite(rect, sprite); }

    public static void button(Rect r, Label label) {
        if(!r.contains(app.input.getMousePos())) {
            box(r, spriteButton);
            label(r, label);
            return;
        }

        box(r, spriteButtonHover);
        label(r, label);
        app.input.isButtonPressed(0);
    }

    public static void button(Rect r, Label label, Sprite sprite) {
        if(!r.contains(app.input.getMousePos())) {
            box(r, sprite);
            label(r, label);
            return;
        }

        box(r, sprite);
        label(r, label);
        app.input.isButtonPressed(0);
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
