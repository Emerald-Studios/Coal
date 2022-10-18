package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.graph.text.Font;
import dk.sebsa.coal.graph.text.Label;
import dk.sebsa.coal.math.Color;
import dk.sebsa.coal.stoneui.Element;

/**
 * @author sebs
 */
public class Text extends Element<Text> {
    private Label label;
    private String text;
    private Color color;
    private Font font;

    public static Font defaultFont;
    public static Font getDefaultFont() {
        if(defaultFont == null) defaultFont = AssetManager.getAsset("internal/DefaultTextFont.fnt");
        return defaultFont;
    }

    public Text(String text) {
        this.text = text;
        this.font = getDefaultFont();
        this.color = Color.white;
    }

    public Text text(String text) {
        this.text = text;
        return this;
    }

    public Text color(Color color) {
        this.color = color;
        return this;
    }

    public Text font(Font font) {
        this.font = font;
        return this;
    }

    public Label getLabel() {
        if (label == null) label = new Label(text, font, color);
        return label;
    }

    @Override
    protected void render() {
        GUI.label(rect, getLabel());
    }
}
