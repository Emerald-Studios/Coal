package dk.sebsa.coal.audio;

import dk.sebsa.Coal;
import dk.sebsa.coal.math.Time;
import dk.sebsa.coal.tasks.Task;

/**
 * @author sebs
 */
public class AudioUpdateTask extends Task {
    private static float cleanTimer = 5;
    @Override
    protected String name() { return getClass().getSimpleName(); }

    @Override
    public void run() {
        AudioManager.update();
        if((AudioManager.statusAll() - AudioManager.statusUsed()) > AudioManager.statusAll() * 0.5f) {
            cleanTimer -= Time.getUnscaledDelta();

            if(cleanTimer <= 0) {
                if(Coal.TRACE) log("AudioManager TrashCollection " + AudioManager.statusAll() + " -> " + AudioManager.statusUsed());
                AudioManager.forceClean();
                cleanTimer = 5;
            }
        } else cleanTimer = 5;
    }
}
