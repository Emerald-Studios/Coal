package dk.sebsa.coal.util;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author sebs
 */
public class Random {

    @Contract(pure = true)
    public static int getInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max) + min;
    }

    @Contract(pure = true)
    public static int getInt(int max) {
        return ThreadLocalRandom.current().nextInt(max);
    }

    @Contract(pure = true)
    public static int getInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    @Contract(pure = true)
    public static boolean getBool() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    @Contract(pure = true)
    public static float getFloat(int min, int max) {
        return min + ThreadLocalRandom.current().nextFloat() * (max - min);
    }

    @Contract(pure = true)
    public static float getFloat(int max) {
        return ThreadLocalRandom.current().nextFloat() * max;
    }

    @Contract(pure = true)
    public static float getFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }
}
