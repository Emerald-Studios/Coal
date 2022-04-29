package dk.sebsa.coal.graph.text;

import lombok.Getter;

/**
 * @author sebs
 */
public class Label {
    @Getter  private final char[] charArray;
    @Getter private final String text;
    @Getter private final Font font;

    public Label(String text, Font font) {
        charArray = text.toCharArray();
        this.text = text;
        this.font = font;
    }
}
