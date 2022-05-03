package dk.sebsa.coal.graph;

import dk.sebsa.coal.math.Vector2f;
import org.jetbrains.annotations.Contract;

/**
 * @author sebs
 * @since 1.0.0
 */
public class Rect {
    public float x;
    public float y;
    public float width;
    public float height;

    public Rect(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public boolean inRect(Vector2f v) {
        return v.x > x && v.x < x+width && v.y > y && v.y < y+height;
    }

    @Contract(pure = true)
    public boolean intersects(Rect r) {
        return !(x > r.x + r.width || x + width < r.x || y > r.y + r.height || y + height < r.y);
    }

    @Contract(pure = true)
    public void getIntersection(Rect r, Rect output) {
        if(!intersects(r)) return;
        float vx = Math.max(x, r.x);
        float vy = Math.max(y, r.y);
        output.set(vx, vy, Math.min(x + width, r.x + r.width) - vx, Math.min(y + height, r.y + r.height) - vy);
    }

    @Contract(value = "_, _, _, _ -> this")
    public Rect set(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        return this;
    }

    @Contract(value = "_ -> this")
    public Rect set(Rect r) {
        return set(r.x, r.y, r.width, r.height);
    }

    @Contract(value = "_, _, _, _ -> this")
    public Rect add(float x, float y, float w, float h) {
        this.x += x;
        this.y += y;
        this.width += w;
        this.height += h;
        return this;
    }

    @Contract(value = "_ -> this")
    public Rect add(Rect r) {
        this.x += r.x;
        this.y += r.y;
        this.width += r.width;
        this.height += r.height;
        return this;
    }

    public static Rect add(Rect r1, Rect r2) {
        return new Rect(r1.x+r2.x, r1.y+r2.y, r1.width+r2.width, r1.height+r2.height);
    }

    public Rect addToNew(Rect r) {
        return new Rect(x,y,width,height).add(r);
    }

    public void addPosition(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void addPosition(Rect r) {
        this.x += r.x;
        this.y += r.y;
    }

    public Rect(Vector2f pos, Vector2f scale) {
        x = pos.x;
        y = pos.y;
        width = scale.x;
        height = scale.y;
    }

    public Rect() {
        this(0, 0, 0, 0);
    }

    @Contract(pure = true)
    public boolean contains(Vector2f v) {return v.x > x && v.x < x + width && v.y > y && v.y < y + height;}
    public Vector2f getSize() {return new Vector2f(width, height);}

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + width + ", " + height + ")";
    }
}
