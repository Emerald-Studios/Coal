package dk.sebsa.coal.math;

import dk.sebsa.coal.util.FourKeyHashMap;
import org.jetbrains.annotations.Contract;

/**
 * @author sebs
 * @since 1.0.0
 */
public class Color {
    public final float r;
    public final float g;
    public final float b;
    public final float a;
    private static final FourKeyHashMap<Float, Float, Float, Float, Color> colorPool = new FourKeyHashMap<>();

    private Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    // Pooling
    @Contract(value = "_, _, _ -> !null", pure = true)
    public static Color color(float r, float g, float b) { return color(r, g, b,1); }
    @Contract(value = "_, _, _,_ -> !null", pure = true)
    public static Color color(float r, float g, float b, float a) {
        r = Mathf.clamp(r, 0, 1);
        g = Mathf.clamp(g, 0, 1);
        b = Mathf.clamp(b, 0, 1);
        a = Mathf.clamp(a, 0, 1);
        return colorPool.getPut(a, r, g, b, new Color(r, g, b, a));
    }
    // Pooling

    // Defaults
    public static final Color black = color(0, 0, 0);
    public static final Color white = color(1, 1, 1);
    public static final Color red =  color(1, 0, 0);
    public static final Color green =  color(0, 1, 0);
    public static final Color blue =  color(0, 0, 1);
    public static final Color grey =  color(0.5f, 0.5f, 0.5f);
    public static final Color dimGrey =  color(0.35f, 0.35f, 0.35f);
    public static final Color darkGrey =  color(0.3f, 0.3f, 0.3f);
    public static final Color wine =  color(0.5f, 0, 0);
    public static final Color forest =  color(0, 0.5f, 0);
    public static final Color marine =  color(0, 0, 0.5f);
    public static final Color yellow =  color(1, 1, 0);
    public static final Color cyan =  color(0, 1, 1);
    public static final Color magenta =  color(1, 0, 1);
    public static final Color transparent =  color(0, 0, 0, 0);
    // Defaults

    public String toString() {
        return "("+r+", "+g+", "+b+", "+a+")";
    }

    @Contract(value = "_ -> !null", pure = true)
    public static float[] toFloatArray(Color c) {
        float[] colors = new float[3];
        colors[0] = c.r;
        colors[1] = c.g;
        colors[2] = c.b;
        return colors;
    }

    @Contract(value = "_ -> !null", pure = true)
    public static Color fromFloatArray(float[] c) {
        return color(c[0], c[1], c[2]);
    }

    @Contract(value = "_ -> !null", pure = true)
    public static Color parseColor(String name) {
        if(name.startsWith("#")) {
            java.awt.Color c = java.awt.Color.decode(name);

            return color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        }

        return switch (name) {
            case "black" -> black;
            case "red" -> red;
            case "green" -> green;
            case "blue" -> blue;
            case "grey" -> grey;
            case "dimGrey" -> dimGrey;
            case "darkGrey" -> darkGrey;
            case "wine" -> wine;
            case "forest" -> forest;
            case "marine" -> marine;
            case "yellow" -> yellow;
            case "cyan" -> cyan;
            case "magenta" -> magenta;
            case "transparent" -> transparent;
            default -> white;
        };
    }
}
