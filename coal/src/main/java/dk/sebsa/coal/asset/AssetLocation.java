package dk.sebsa.coal.asset;

import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author sebs
 * @since 1.0.0
 */
public record AssetLocation(AssetLocationType locationType, String location) {
    public static final AssetLocation none = new AssetLocation(AssetLocationType.Code, "");

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

    public ByteBuffer asBuffer(int bufferSize) {
        try {
            return FileUtils.isToBB(asStream(), bufferSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
