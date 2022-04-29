package dk.sebsa.coal.asset;

import java.util.List;

/**
 * @author sebs
 * @since 1.0.0
 */
public abstract class AssetProvider {
    protected abstract List<AssetLocation> getAssets();
}
