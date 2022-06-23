package dk.sebsa.coal.trash;

import dk.sebsa.Coal;
import dk.sebsa.coal.audio.AudioManager;
import dk.sebsa.coal.audio.AudioUpdateTask;
import dk.sebsa.coal.math.Color;
import dk.sebsa.coal.math.Time;
import dk.sebsa.coal.tasks.Task;

/**
 * @author sebs
 */
public class TCFrameTask extends Task {
    private static float audioCleanTimer = 5;
    private static float colorPruneTimer = 60 * 5;
    @Override
    protected String name() { return "TCTaskMain"; }

    @Override
    public void run() {
        // tcAudioClean
        if(Coal.getCapabilities().tcAudioClean && (AudioManager.statusAll() - AudioManager.statusUsed()) > AudioManager.statusAll() * 0.5f && AudioManager.statusAll() > 1) {
            audioCleanTimer -= Time.getUnscaledDelta();

            if(audioCleanTimer <= 0) {
                if(Coal.TRACE) log("{Trash} Audio Prune on Next Frame");
                AudioUpdateTask.cleanOnNextFrame = true;
                audioCleanTimer = 5;
            }
        } else audioCleanTimer = 5;

        // tcColorPrune
        if(Coal.getCapabilities().tcColorPrune) {
            colorPruneTimer -= Time.getUnscaledDelta();

            if(colorPruneTimer <= 0) {
                if(Coal.TRACE) log("{Trash} Color Prune");
                Color.getColorPool().prune();
                colorPruneTimer = 60 * 5;
            }
        }
    }
}
