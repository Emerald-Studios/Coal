package dk.sebsa.coal.math;


/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class Color {
    public float r;
    public float g;
    public float b;
    public float a;

    public Color(float r, float g, float b, float a) {
        this.r = Mathf.clamp(r, 0, 1);
        this.g = Mathf.clamp(g, 0, 1);
        this.b = Mathf.clamp(b, 0, 1);
        this.a = Mathf.clamp(a, 0, 1);
    }

    public Color(float r, float g, float b) {
        this.r = Mathf.clamp(r, 0, 1);
        this.g = Mathf.clamp(g, 0, 1);
        this.b = Mathf.clamp(b, 0, 1);
        this.a = 1;
    }

    public static Color black() { return new Color(0, 0, 0);}
    public static Color white() { return new Color(1, 1, 1);}
    public static Color red() { return new Color(1, 0, 0);}
    public static Color green() { return new Color(0, 1, 0);}
    public static Color blue() { return new Color(0, 0, 1);}
    public static Color grey() { return new Color(0.5f, 0.5f, 0.5f);}
    public static Color dimGrey() { return new Color(0.35f, 0.35f, 0.35f);}
    public static Color darkGrey() { return new Color(0.3f, 0.3f, 0.3f);}
    public static Color wine() { return new Color(0.5f, 0, 0);}
    public static Color forest() { return new Color(0, 0.5f, 0);}
    public static Color marine() { return new Color(0, 0, 0.5f);}
    public static Color yellow() { return new Color(1, 1, 0);}
    public static Color cyan() { return new Color(0, 1, 1);}
    public static Color magenta() { return new Color(1, 0, 1);}
    public static Color transparent() { return new Color(0, 0, 0, 0);}

    public String toString() {
        return "("+r+", "+g+", "+b+", "+a+")";
    }

    public boolean compare(Color c) {
        return c.r == r && c.g == g && c.b == b && c.a == a;
    }

    public static float[] toFloatArray(Color c) {
        float[] colors = new float[3];
        colors[0] = c.r;
        colors[1] = c.g;
        colors[2] = c.b;
        return colors;
    }

    public static Color fromFloatArray(float[] c) {
        return new Color(c[0], c[1], c[2]);
    }

    public static Color parseColor(String name) {
        if(name.startsWith("#")) {
            java.awt.Color c = java.awt.Color.decode(name);

            return new Color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        }

        return switch (name) {
            case "black" -> black();
            case "red" -> red();
            case "green" -> green();
            case "blue" -> blue();
            case "grey" -> grey();
            case "dimGrey" -> dimGrey();
            case "darkGrey" -> darkGrey();
            case "wine" -> wine();
            case "forest" -> forest();
            case "marine" -> marine();
            case "yellow" -> yellow();
            case "cyan" -> cyan();
            case "magenta" -> magenta();
            case "transparent" -> transparent();
            default -> white();
        };
    }
}
