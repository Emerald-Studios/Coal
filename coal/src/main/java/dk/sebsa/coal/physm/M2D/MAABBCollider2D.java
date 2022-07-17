package dk.sebsa.coal.physm.M2D;

import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;
import dk.sebsa.coal.math.Vector2f;
import dk.sebsa.coal.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebs
 */
public class MAABBCollider2D extends MCollider2D {
    public Vector2f size = new Vector2f(100, 100);

    public MAABBCollider2D() {

    }

    public MAABBCollider2D(boolean trigger) {
        isTrigger = trigger;
    }

    public MAABBCollider2D(SpriteRenderer renderer) {
        size = renderer.sprite.getOffset().getSize().mul(renderer.scale);
    }

    public MAABBCollider2D setTrigger(boolean trigger) { isTrigger = trigger; return this; }

    public List<Vector3f> getCorners() {
        List<Vector3f> corners = new ArrayList<>();
        Vector3f anchorPos = transform.getGlobalPosition().add(anchor);
        corners.add(anchorPos);
        corners.add(anchorPos.add(new Vector2f(0, size.y)));
        corners.add(anchorPos.add(size));
        corners.add(anchorPos.add(new Vector2f(size.x, 0)));
        return corners;
    }

    private final Rect worldPositionRect = new Rect();
    public Rect getWorldPositionRect() {
        float halfW = size.x * anchor.x;
        float halfH = size.y * anchor.y;
        return worldPositionRect.set(transform.getGlobalPosition().x + halfW, transform.getGlobalPosition().y - halfH, size.x, size.y);
    }
}
