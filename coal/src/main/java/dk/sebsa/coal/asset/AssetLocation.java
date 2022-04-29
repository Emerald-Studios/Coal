package dk.sebsa.coal.asset;

import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;

public record AssetLocation(AssetLocationType locationType, String location) {
    public static AssetLocation none = new AssetLocation(AssetLocationType.Code, "");

    public InputStream asStream() {
        if (locationType.equals(AssetLocationType.LocalFile) || locationType.equals(AssetLocationType.Jar)) {
            try {
                return FileUtils.loadFile(location);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
