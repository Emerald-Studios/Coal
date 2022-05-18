package dk.sebsa.coal.asset;

import dk.sebsa.Coal;
import dk.sebsa.coal.trash.Trash;

import java.util.Objects;

/**
 * @author sebs
 * @since 1.0.0
 */
public abstract class Asset extends Trash {
    public final AssetLocation location;
    public String name = "Unnamed";

    protected void log(Object o) { Coal.logger.log(o.toString(), this.getClass().getSimpleName()); }

    public Asset(AssetLocation location) {
        this.location = location;
        AssetManager.loadedAssets.add(this);
    }

    public Asset name() throws AssetExitsException {
        // Name Generation
        name = location.location();
        if(name.startsWith("/coal")) name = name.replaceFirst("/coal/", "");
        name = name.replaceAll("\\\\", "/");

        if(AssetManager.getAssetNameMap().containsKey(name)) throw new AssetExitsException();
        else AssetManager.getAssetNameMap().put(name, this);

        return this;
    }

    public Asset loadAsset() throws AssetExitsException {
        if(Coal.TRACE) log("Creating Asset! " + location + ", Name: " + name);
        if(Objects.equals(name, "Unnamed")) name();

        load();
        return this;
    }

    protected abstract void load();
}
