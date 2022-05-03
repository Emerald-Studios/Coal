package dk.sebsa.coal.audio;

import dk.sebsa.coal.tasks.Task;

/**
 * @author sebs
 */
public class AudioUpdateTask extends Task {
    @Override
    protected String name() { return getClass().getSimpleName(); }

    @Override
    public void run() {
        AudioManager.update();
    }
}
