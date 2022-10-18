package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.graph.text.Font;
import dk.sebsa.coal.graph.text.Label;
import dk.sebsa.coal.io.ButtonPressedEvent;
import dk.sebsa.coal.math.Color;
import dk.sebsa.coal.stoneui.Element;

public class TextField extends Element<TextField> {
    public final String[] value = new String[1];
    private String prefix;

    private final Rect clickRect = new Rect();
    private boolean editing = false;
    private Font font;

    public TextField prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public TextField(String currentValue, Font f) {
        value[0] = currentValue;
        font = f;
        this.sprite = GUI.spriteButton;
    }

    public TextField(String currentValue) {
        value[0] = currentValue;
        font = Text.getDefaultFont();
        this.sprite = GUI.spriteButton;
    }

    public TextField setFont(Font f) {
        this.font = f;
        return this;
    }

    @Override
    public void render() {
        GUI.button(rect, new Label(prefix + value[0], font, Color.white), sprite, sprite);
    }

    @Override
    public boolean handleEvent(Event e) {
        if(e.eventType() != EventTypes.ButtonPressed || clickRect.isZero()) return false;
        ButtonPressedEvent event = (ButtonPressedEvent) e;

        if(event.button == 0) {
            if(clickRect.contains(event.mouse)) {
                editing = false;
                return true;
            } else {
                editing = false;
            }
        }
        return false;
    }
}