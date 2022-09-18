package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.io.ButtonPressedEvent;
import dk.sebsa.coal.stoneui.Element;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * @author sebs
 */
public class Button extends Element<Button> {
    @Getter private final Text text;
    private final Consumer<Button> onClick;
    private final Rect clickRect = new Rect();
    private Sprite spriteHover;

    public Button(Text text, Consumer<Button> onClick) {
        this.text = text;
        this.onClick = onClick;
        this.spriteHover = GUI.spriteButtonHover;
        this.sprite = GUI.spriteButton;
    }

    public Button(Consumer<Button> onClick) {
        this.text = null;
        this.onClick = onClick;
        this.spriteHover = GUI.spriteButtonHover;
        this.sprite = GUI.spriteButton;
    }

    private float textOffset = 0;
    public Button centerText() {
        assert text != null;
        textOffset = (getWidth() / 2 - (text.getLabel().getFont().getStringWidth(text.getLabel().getText())*1.0f / 2f));
        if(textOffset % 1 != 0) textOffset -= textOffset % 1;
        return this;
    }

    @Override
    protected void render() {
        clickRect.set(rect);
        if(text == null) {
            GUI.button(rect, null, sprite, spriteHover);
        } else if(textOffset == 0)
            GUI.button(rect, text.getLabel(), sprite, spriteHover);
        else
            GUI.button(rect, text.getLabel(), sprite, spriteHover,textOffset);
    }

    public Button hoverSprite(Sprite hoverSprite) {
        spriteHover = hoverSprite;
        return this;
    }

    @Override
    public boolean handleEvent(Event e) {
        if(e.eventType() != EventTypes.ButtonPressed || clickRect.isZero()) return false;
        ButtonPressedEvent event = (ButtonPressedEvent) e;

        if(event.button == 0 && clickRect.contains(event.mouse)) {
            if(onClick != null) onClick.accept(this);
            return true;
        }
        return false;
    }
}
