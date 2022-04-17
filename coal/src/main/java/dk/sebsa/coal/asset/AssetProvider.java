package dk.sebsa.coal.asset;

import java.util.List;

public abstract class AssetProvider {
    protected abstract List<AssetLocation> getAssets();
}
