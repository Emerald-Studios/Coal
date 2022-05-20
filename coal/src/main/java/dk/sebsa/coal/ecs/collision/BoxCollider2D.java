package dk.sebsa.coal.ecs.collision;

import dk.sebsa.Coal;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.math.Vector2f;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author sebs
 */
public class BoxCollider2D extends Collider2D {
    @Getter private final Vector2f anchor;
    @Getter private final Vector2f size;

    private final Rect worldPositionRect = new Rect();
    private SpriteRenderer renderer;

    public BoxCollider2D() {
        size = new Vector2f(100, 100);
        anchor = new Vector2f(0.5f, 0.5f);
    }

    public BoxCollider2D(SpriteRenderer renderer) {
        size = renderer.sprite.getOffset().getSize();
        anchor = new Vector2f(0.5f, 0.5f);
    }

    @Override
    protected void load() {
        if(!Coal.getCapabilities().coalPhysics2D) entity.removeComponent(this);
    }

    private BoxCollider2D calcGlobalPos() {
        float halfW = size.x * anchor.x;
        float halfH = size.y * anchor.y;
        worldPositionRect.set(transform.getGlobalPosition().x - halfW, transform.getGlobalPosition().y + halfH, size.x, size.y);

        return this;
    }

    @Override
    protected void update(GLFWInput input) {

    }

    @Override
    protected void lateUpdate(GLFWInput input) {

    }

    public Rect getWorldPositionRect() {
        calcGlobalPos();

        return worldPositionRect;
    }

    private final Vector2f calcVec = new Vector2f();
    @Override
    public boolean collides(@NotNull BoxCollider2D collider2D) {
        calcGlobalPos();
        return collider2D.calcGlobalPos().worldPositionRect.overlap(worldPositionRect);
    }
}
