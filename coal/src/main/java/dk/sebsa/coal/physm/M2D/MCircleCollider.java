package dk.sebsa.coal.physm.M2D;

import dk.sebsa.coal.math.Vector3f;

/**
 * @author sebs
 */
public class MCircleCollider extends MCollider2D {
    public float radius = 16;

    public Vector3f center() { return transform.getGlobalPosition().add(anchor); }
}
