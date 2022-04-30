package dk.sebsa.coal.graph;

import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.util.FileUtils;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.List;

/**
 * @author sebs
 */
public class Sprite extends Asset {
    @Getter private Rect offset;
    @Getter private Rect padding;
    @Getter private Material material;

    public Sprite(AssetLocation location) {
        super(location);
    }

    public Sprite(String name, Rect offset, Rect padding, Material material) {
        super(AssetLocation.none);
        this.name = name;
        this.offset = offset;
        this.padding = padding;
        this.material = material;

        log(" * Creating sprite " + name);
        AssetManager.getAssetNameMap().put(name, this);
    }

    @Override
    public void destroy() {

    }

    @Override @SneakyThrows
    protected void load() {
        List<String> raw = FileUtils.readAllLinesList(location.asStream());
        for(String line : raw) {
            if(line.startsWith("m")) material = (Material) AssetManager.getAsset(line.split(":")[1]);
            else if(line.startsWith("o")) {
                String[] e = line.split(":")[1].split(",");
                offset = new Rect(Float.parseFloat(e[0]),Float.parseFloat(e[1]),Float.parseFloat(e[2]),Float.parseFloat(e[3]));
            } else if(line.startsWith("p")) {
                String[] e = line.split(":")[1].split(",");
                padding = new Rect(Float.parseFloat(e[0]),Float.parseFloat(e[1]),Float.parseFloat(e[2]),Float.parseFloat(e[3]));
            }
        }
    }

    private final Rect uv = new Rect(0,0,0,0);
    public Rect getUV() {
        if(offset == null) return null;
        if(material.getTexture() == null) return uv;

        float w = material.getTexture().getWidth();
        float h = material.getTexture().getHeight();
        return uv.set(offset.x / w, offset.y / h, offset.width / w, offset.height / h);
    }

    private final Rect paddingUV = new Rect(0,0,0,0);
    public Rect getPaddingUV() {
        if(padding == null) return null;
        if(material.getTexture() == null) return paddingUV;

        float w = material.getTexture().getWidth();
        float h = material.getTexture().getHeight();
        return paddingUV.set(padding.x / w, padding.y / h, padding.width / w, padding.height / h);
    }
}
