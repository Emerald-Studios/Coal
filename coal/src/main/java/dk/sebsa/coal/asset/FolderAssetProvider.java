package dk.sebsa.coal.asset;

import java.util.List;

public class FolderAssetProvider extends AssetProvider {
    private final String folder;

    public FolderAssetProvider(String folder) {
        this.folder = folder;
    }

    @Override
    protected List<AssetLocation> getAssets() {
        return null;
    }
}
