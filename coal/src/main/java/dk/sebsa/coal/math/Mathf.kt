package dk.sebsa.coal.math

import org.jetbrains.annotations.Contract

/**
 * @author sebs
 * @since 1.0.0
 */
object Mathf {
    @JvmStatic
    private var remainder = 0f

    @JvmStatic @Contract(value = "_, _, _, -> !null", pure = true)
    fun clamp(`val`: Float, min: Float, max: Float): Float {
        if (`val` < min) return min
        return if (`val` > max) max else `val`
    }

    @JvmStatic @Contract(value = "_, _, _ -> !null", pure = true)
    fun wrap(`val`: Float, min: Float, max: Float): Float {
        remainder = max - min
        return ((`val` - min) % remainder + remainder) % remainder + min
    }

    @JvmStatic @Contract(value = "_ -> !null", pure = true)
    fun isVectorZero(v: Vector3f): Boolean {
        if (v.x != 0f) return false
        if (v.y != 0f) return false
        return v.z == 0f
    }

    @JvmStatic @Contract(value = "_ -> !null", pure = true)
    fun abs(`val`: Float): Float {
        return kotlin.math.abs(`val`)
    }
}