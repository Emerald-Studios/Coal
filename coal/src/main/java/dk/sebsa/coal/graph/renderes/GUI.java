package dk.sebsa.coal.graph.renderes;

import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.graph.SpriteSheet;

/**
 * @author sebs
 * @since 1.0.0
 */
public class GUI {
    private static SpriteSheet sheet;
    private static Sprite spriteBox;

    public static void prepare(SpriteSheet spriteSheet) {
        if(!Core2D.isPrepared()) Core2D.prepare();
        sheet = spriteSheet;
        spriteBox = sheet.getSprite("Box");
    }

    public static void box(Rect rect) {
        Core2D.drawSprite(rect, spriteBox);
    }

    public static void unprepare() {
        if(Core2D.isPrepared()) Core2D.unprepare();
    }
}
