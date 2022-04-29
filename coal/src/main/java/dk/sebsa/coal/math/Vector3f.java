package dk.sebsa.coal.math;

/**
 * @author sebs
 * @since 1.0.0
 */
public class Vector3f {
    public float x;
    public float y;
    public float z;

    public Vector3f(Vector3f v) { x = v.x; y = v.y; z = v.z; }
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3f(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }
    public void zero() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3f set(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
}
