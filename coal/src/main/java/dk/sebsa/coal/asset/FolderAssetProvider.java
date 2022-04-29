package dk.sebsa.coal.asset;

import dk.sebsa.Coal;
import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sebs
 * @since 1.0.0
 */
public class FolderAssetProvider extends AssetProvider {
    private final File folder;

    public FolderAssetProvider(String folderPath) {
        this.folder = new File(folderPath);
        if(!folder.isDirectory()) {
            Coal.logger.error("Path is not directory: " + folderPath, "FolderAssetProvider");
            Coal.shutdownDueToError();
        }
    }

    @Override
    protected List<AssetLocation> getAssets() {
        List<AssetLocation> locations = new ArrayList<>();
        for(File f : FileUtils.listFilesInFolder(folder)) {
            locations.add(new AssetLocation(AssetLocationType.LocalFile, f.getPath()));
        } return locations;
    }
}
