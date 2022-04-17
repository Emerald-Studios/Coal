package dk.sebsa.coal.asset;

import dk.sebsa.coal.tasks.Task;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class AssetManagerInitTask extends Task {
    @Override
    protected String name() { return getClass().getSimpleName(); }

    @Override
    public void run() {
        AssetManager.initGetAllAssets(logConsumer);
        AssetManager.initCreateAllAssets(logConsumer);
    }
}
