package dk.sebsa.coal.util;

import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import lombok.Getter;

import java.io.IOException;

public class ConfigAsset extends Asset {
    @Getter
    private String rawConfig;

    public ConfigAsset(AssetLocation location) {
        super(location);
    }

    @Override
    public void destroy() {
        
    }

    @Override
    protected void load() {
        try {
            rawConfig = FileUtils.readAllLines(location.asStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
