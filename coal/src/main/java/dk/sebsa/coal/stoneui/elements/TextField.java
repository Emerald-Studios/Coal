package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.graph.text.Font;
import dk.sebsa.coal.graph.text.Label;
import dk.sebsa.coal.io.ButtonPressedEvent;
import dk.sebsa.coal.io.CharEvent;
import dk.sebsa.coal.io.KeyPressedEvent;
import dk.sebsa.coal.math.Color;
import dk.sebsa.coal.stoneui.Element;
import org.lwjgl.glfw.GLFW;

public class TextField extends Element<TextField> {
    public final String[] value;
    private String prefix;

    private final Rect clickRect = new Rect();
    private boolean editing = false;
    private Font font;

    public TextField prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public TextField(String[] currentValue, Font f) {
        value = currentValue;
        font = f;
        this.sprite = GUI.spriteButton;
    }

    public TextField(String[] currentValue) {
        value = currentValue;
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
        clickRect.set(rect);
    }

    @Override
    public boolean handleEvent(Event e) {
        if(e.eventType() == EventTypes.ButtonPressed) {
            if(clickRect.isZero()) return false;
            ButtonPressedEvent event = (ButtonPressedEvent) e;

            if(clickRect.contains(event.mouse) && event.button == 0) {
                editing = true;
                return true;
            } else {
                editing = false;
            }
        } if(editing) {
            if(e.eventType() == EventTypes.Char) {
                value[0] += (char) ((CharEvent) e).codePoint;
                return true;
            } else if(e.eventType() == EventTypes.KeyPressed) {
                KeyPressedEvent e2 = (KeyPressedEvent) e;
                String v = value[0];
                if(e2.key == GLFW.GLFW_KEY_BACKSPACE && v.length() > 0) value[0] = v.substring(0, v.length()-1);
            }
        }
        return false;
    }
}