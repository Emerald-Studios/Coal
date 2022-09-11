package dk.sebsa.coal.stoneui;

import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.stoneui.elements.Group;
import lombok.Getter;

/**
 * @author sebs
 */
public abstract class Element<T extends Element<T>> {
    @Getter
    private float posX, posY;
    @Getter private float width = 100, height = 100;
    public Group parent;
    protected Sprite sprite;

    private Padding padding;
    private static final Padding zeroPadding = new Padding();
    public Padding getPadding() {
        if(padding == null) return zeroPadding;
        return padding;
    }

    protected Rect rect = new Rect();
    protected abstract void render();

    public void draw(Rect offset) {
        rect.set(posX, posY, width, height);
        if(parent != null) rect.addPosition(parent.rect);
        rect.addPosition(offset);
        
        render();
    }

    public T pos(float x, float y) {
        posX = x;
        posY = y;
        return (T) this;
    }

    public T size(float w, float h) {
        width = w;
        height = h;
        return (T) this;
    }

    public T rect(Rect r) {
        posX = r.x;
        posY = r.y;
        width = r.width;
        height = r.height;
        return (T) this;
    }

    public T padding(int i) {
        if(padding == null) padding = new Padding();
        padding.set(i);
        return (T) this;
    }

    public T padding(int top, int bot, int left, int right) {
        if(padding == null) padding = new Padding();
        padding.set(top, bot, left, right);
        return (T) this;
    }

    public T sprite(Sprite sprite) { // USE NULL FOR DEFAULT
        this.sprite = sprite;
        return (T) this;
    }

    public boolean handleEvent(Event e) {
        // DO NOTHING!!
        return false;
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + "]";
    }
}
