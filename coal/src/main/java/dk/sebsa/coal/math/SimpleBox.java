package dk.sebsa.coal.math;

/**
 * @author sebs
 */
public class SimpleBox {
    public Vector2f corner1, corner2;

    public SimpleBox(Vector2f corner1, Vector2f corner2) {
        this.corner1 = corner1;
        this.corner2 = corner2;
    }

    public SimpleBox(Vector2f corner) {
        this.corner1 = corner;
        this.corner2 = corner;
    }

    public SimpleBox(float c1x, float c1y, float c2x, float c2y) {
        this.corner1 = new Vector2f(c1x, c1y);
        this.corner2 = new Vector2f(c2x, c2y);
    }

    public SimpleBox() {
        this.corner1 = new Vector2f();
        this.corner2 = new Vector2f();
    }

    public SimpleBox set(float c1x, float c1y, float c2x, float c2y) {
        corner1.set(c1x, c1y);
        corner2.set(c2x, c2y);

        return this;
    }
}
