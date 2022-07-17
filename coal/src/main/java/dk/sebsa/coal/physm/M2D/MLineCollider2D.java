package dk.sebsa.coal.physm.M2D;

import dk.sebsa.coal.math.Vector2f;
import dk.sebsa.coal.math.Vector3f;

/**
 * @author sebs
 */
public class MLineCollider2D extends MCollider2D {
    public Vector2f start = new Vector2f();
    public Vector2f end = new Vector2f(16, 0);

    public Vector3f getWorldStart() {return transform.getGlobalPosition().add(start);}
    public Vector3f getWorldEnd() {return transform.getGlobalPosition().add(end);}
}
