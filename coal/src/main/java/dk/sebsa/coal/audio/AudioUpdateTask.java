package dk.sebsa.coal.audio;

import dk.sebsa.Coal;
import dk.sebsa.coal.tasks.Task;

/**
 * @author sebs
 */
public class AudioUpdateTask extends Task {
    public static boolean cleanOnNextFrame = false;

    @Override
    protected String name() { return getClass().getSimpleName(); }

    @Override
    public void run() {
        AudioManager.update();
        if(cleanOnNextFrame) {
            cleanOnNextFrame = false;
            if(Coal.TRACE) log("Source Prune" + AudioManager.statusAll() + " -> " + AudioManager.statusUsed());
            AudioManager.forceClean();
        }
    }
}
