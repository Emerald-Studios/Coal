package dk.sebsa.coal.graph;

import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.math.Color;
import dk.sebsa.coal.util.FileUtils;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.List;

/**
 * @author sebs
 */
public class Material extends Asset {
    @Getter private boolean isTextured;
    @Getter private Texture texture;
    @Getter private Color color;

    public Material(AssetLocation location) {
        super(location);
    }

    @Override
    public void destroy() {

    }

    @Override @SneakyThrows
    protected void load() {
        List<String> raw = FileUtils.readAllLinesList(location.asStream());
        for(String line : raw) {
            if(line.startsWith("t")) texture = (Texture) AssetManager.getAsset(line.split(":")[1]);
            else if(line.startsWith("c")) {
                String[] e = line.split(":")[1].split(",");
                color = Color.color(Float.parseFloat(e[0]),Float.parseFloat(e[1]),Float.parseFloat(e[2]),Float.parseFloat(e[3]));
            }
        }
    }

    public Material(Texture texture) {
        super(AssetLocation.none);
        this.texture = texture;
        this.color = Color.white;
        this.isTextured = true;
    }

    public Material(Color color) {
        super(AssetLocation.none);
        this.color = color;
        this.isTextured = false;
    }
}
