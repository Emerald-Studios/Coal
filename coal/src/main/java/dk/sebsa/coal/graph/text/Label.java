package dk.sebsa.coal.graph.text;

import dk.sebsa.coal.graph.Material;
import dk.sebsa.coal.math.Color;
import lombok.Getter;

/**
 * @author sebs
 */
public class Label {
    @Getter  private final char[] charArray;
    @Getter private final String text;
    @Getter private final Font font;
    @Getter private final Color color;
    private Material mat;

    public Material getMaterial() {
        if(mat == null) mat = new Material(font.getTexture(), color);
        return mat;
    }

    public Label(String text, Font font, Color color) {
        charArray = text.toCharArray();
        this.text = text;
        this.font = font;
        this.color = color;
    }
}
