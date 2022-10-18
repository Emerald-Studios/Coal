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
    private int cursorPos;

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

    private static final Rect r = new Rect();
    @Override
    public void render() {
        Label label = new Label(prefix + value[0], font, Color.white);
        GUI.button(rect, label, sprite, sprite);
        clickRect.set(rect);

        if(editing) {
            float x = getCursorX(label, cursorPos+prefix.length()-1);
            GUI.box(r.set(rect.x + x, rect.y-1, 1, rect.height -2), GUI.spritePixel);
        }
    }

    @Override
    public boolean handleEvent(Event e) {
        if(e.eventType() == EventTypes.ButtonPressed) {
            if(clickRect.isZero()) return false;
            ButtonPressedEvent event = (ButtonPressedEvent) e;

            if(clickRect.contains(event.mouse) && event.button == 0) {
                editing = true;

                // Temporarly modify clickRect so prefix is not included in calculations below
                clickRect.add(font.getStringWidth(prefix), 0, -1, 0);
                cursorPos = getCursorPos(new Label(value[0], font, Color.white), event.mouse.x, clickRect);
                return true;
            } else {
                editing = false;
            }
        } if(editing) {
            if(e.eventType() == EventTypes.Char) {
                value[0] = value[0].substring(0, cursorPos) + (char) ((CharEvent) e).codePoint + value[0].substring(cursorPos);
                cursorPos++;
                return true;
            } else if(e.eventType() == EventTypes.KeyPressed) {
                KeyPressedEvent e2 = (KeyPressedEvent) e;
                String v = value[0];
                if(e2.key == GLFW.GLFW_KEY_BACKSPACE && v.length() > 0 && cursorPos > 0) {
                    if(cursorPos < value[0].length()) value[0] = v.substring(0, cursorPos-1) + v.substring(cursorPos);
                    else value[0] = v.substring(0, cursorPos-1);
                    cursorPos--; limit();
                } else if(e2.key == GLFW.GLFW_KEY_DELETE && v.length() > 0 && cursorPos < value[0].length()) {
                    value[0] = v.substring(0, cursorPos) + v.substring(cursorPos+1, value[0].length());
                }
                else if(e2.key == GLFW.GLFW_KEY_LEFT) { cursorPos--; limit(); }
                else if(e2.key == GLFW.GLFW_KEY_RIGHT) { cursorPos++; limit(); }

                return true;
            }
        }
        return false;
    }

    private void limit() {
        if(cursorPos < 0) cursorPos = 0;
        else if (cursorPos > value[0].length()) cursorPos = value[0].length();
    }

    public static float getCursorX(Label label, int cursorPos) {
        if(label.getText().length()==0) return 0;
        String s = label.getText();
        s = s.substring(0, cursorPos+1);
        return label.getFont().getStringWidth(s);
    }

    public static int getCursorPos(Label label, float mouseX, Rect clickRect) {
        String text = label.getText();

        if(mouseX >= clickRect.x+label.getWidth()) return text.length();
        else if(mouseX <= clickRect.x) return 0; // This happens when clicking on prefix part of textfield

        float x = clickRect.x;
        for(int i = 0; i < text.length(); i++) {
            System.out.println(x);
            x += label.getFont().getStringWidth(text.substring(i,i+1));
            if(x >= mouseX) return i;
        }
        return text.length(); // This shouldn't happen but if it does, nothing goes wrong anyway
    }
}