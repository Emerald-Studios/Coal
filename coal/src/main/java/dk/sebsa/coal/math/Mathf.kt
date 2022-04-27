package dk.sebsa.coal.math

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
object Mathf {
    @JvmStatic
    private var remainder = 0f

    @JvmStatic
    fun clamp(`val`: Float, min: Float, max: Float): Float {
        if (`val` < min) return min
        return if (`val` > max) max else `val`
    }

    @JvmStatic
    fun wrap(`val`: Float, min: Float, max: Float): Float {
        remainder = max - min
        return ((`val` - min) % remainder + remainder) % remainder + min
    }

    @JvmStatic
    fun isVectorZero(v: Vector3f): Boolean {
        if (v.x != 0f) return false
        if (v.y != 0f) return false
        return v.z == 0f
    }

    @JvmStatic
    fun abs(`val`: Float): Float {
        return kotlin.math.abs(`val`)
    }
}