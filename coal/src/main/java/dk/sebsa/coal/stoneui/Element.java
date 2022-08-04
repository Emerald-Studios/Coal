package dk.sebsa.coal.stoneui;

import dk.sebsa.coal.graph.Rect;

/**
 * @author sebs
 */
public abstract class Element<T extends Element<T>> {
    private int posX, posY;
    private int width = 100, height = 100;

    protected Rect rect = new Rect();
    protected abstract void render();

    public void draw() {
        rect.set(posX, posY, width, height);
        render();
    }

    public T pos(int x, int y) {
        posX = x;
        posY = y;
        return (T) this;
    }

    public T sixe(int w, int h) {
        width = w;
        height = h;
        return (T) this;
    }
}
