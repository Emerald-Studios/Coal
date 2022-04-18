package dk.sebsa.coal.asset;

import dk.sebsa.Coal;

public abstract class Asset {
    public final AssetLocation location;
    public String name = "Unnamed";

    protected void log(Object o) { Coal.logger.log(o.toString(), this.getClass().getSimpleName()); }

    public Asset(AssetLocation location) {
        this.location = location;
        AssetManager.loadedAssets.add(this);
    }

    public abstract void destroy();

    public Asset loadAsset() throws AssetExitsException {
        // Name Generation
        name = location.location();
        if(name.startsWith("/coal")) name = name.replaceFirst("/coal/", "");

        if(Coal.TRACE) log("Creating Asset! " + location + ", Name: " + name);

        if(AssetManager.getAssetNameMap().containsKey(name)) throw new AssetExitsException();
        else AssetManager.getAssetNameMap().put(name, this);

        load();
        return this;
    }

    protected abstract void load();
}
