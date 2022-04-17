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

    public Asset loadAsset() {
        if(Coal.TRACE) log("Creating Asset! " + location + ", Name: " + name);
        load();
        return this;
    }

    protected abstract void load();
}
