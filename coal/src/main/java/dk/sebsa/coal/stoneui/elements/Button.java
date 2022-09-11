package dk.sebsa.coal.stoneui.elements;

import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.graph.Rect;
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

    public Button(Text text, Consumer<Button> onClick) {
        this.text = text;
        this.onClick = onClick;
    }

    @Override
    protected void render() {
        if(this.sprite == null)
            GUI.button(rect, text.getLabel());
        else GUI.button(rect,text.getLabel(), sprite);
        clickRect.set(rect);
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
