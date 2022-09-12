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
    }

    private float textOffset = 0;
    public Button centerText() {
        textOffset = (getWidth() / 2 - (text.getLabel().getFont().getStringWidth(text.getLabel().getText())*1.0f / 2f));
        return this;
    }

    @Override
    protected void render() {
        if(spriteHover == null)
            this.spriteHover = GUI.spriteButtonHover;
        if(sprite == null) sprite = GUI.spriteButton;

        clickRect.set(rect);
        if(textOffset == 0)
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
