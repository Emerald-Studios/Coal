package dk.sebsa.coal.math;

import java.util.concurrent.TimeUnit;

import dk.sebsa.Coal;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class Time {
    // Constants
    public static final long SECOND = 1000000000L;

    // Settings
    public static float timeScale = 1;

    // Time Values
    private static long startTime; // The moment we began time keeping
    private static long rawTime; // Time value that keeps going up (May be negative)
    private static long frameStartTime; // When this frame started
    private static long lastFrameTime; // When the last frame started
    private static long framePassedTime; // How long the last frame was
    private static long time;

    private static long fpsCountTime; // Time instant when FPS was last counted
    private static int frames; // Frames since last FPS count
    private static int framesPerSecond;
    private static double averageFrameLength;	// The average frame length this second. In millis
    private static double frameTimeThisSecond;

    private static float deltaTime;
    private static float unscaledDelta;
    private static double rawDelta;

    // Methods
    public static void init() {
        Coal.logger.log("Initializing Time system");
        startTime = System.nanoTime();
    }

    public static void procsess() {
        rawTime = System.nanoTime();
        time = (TimeUnit.MILLISECONDS.convert(rawTime - startTime, TimeUnit.NANOSECONDS));

        // Calculate frame time
        frameStartTime = rawTime;
        framePassedTime = frameStartTime - lastFrameTime;
        lastFrameTime = frameStartTime;
        frameTimeThisSecond = frameTimeThisSecond + framePassedTime;

        // Calculate Delta time
        rawDelta = framePassedTime / (double) SECOND;
        if(rawDelta > 0.01f) {
            deltaTime = (float) (0.01 * timeScale);
            unscaledDelta = 0.01f;
        }
        else {
            deltaTime = (float) (rawDelta * timeScale);
            unscaledDelta = (float) rawDelta;
        }

        // FPS Count
        if(time - fpsCountTime >= 1000) {
            framesPerSecond = frames;
            frames = 0;
            fpsCountTime = time;


            averageFrameLength = (frameTimeThisSecond/framesPerSecond/1000000);
            frameTimeThisSecond = 0;
        }
        frames++;
    }

    public static long getTime() {
        rawTime = System.nanoTime();
        return (TimeUnit.MILLISECONDS.convert(rawTime - startTime, TimeUnit.NANOSECONDS));
    }

    public static float getDeltaTime() {
        return deltaTime;
    }

    public static float getUnscaledDelta() {
        return unscaledDelta;
    }

    public static int getFPS() {
        return framesPerSecond;
    }

    /**
     * @return the averageFrameLength
     */
    public static double getAFL() {
        return averageFrameLength;
    }
}
