package dk.sebsa.coal.graph;

import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.util.FileUtils;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sebs
 */
public class SpriteSheet extends Asset {
    @Getter private final List<Sprite> sprites = new ArrayList<>();
    private final Map<String, Sprite> spriteMap = new HashMap<>();
    @Getter private Material material;

    private Rect offset, padding;
    private String s;

    public SpriteSheet(AssetLocation location) {
        super(location);
    }

    @Override
    public void destroy() {

    }

    @Override @SneakyThrows
    protected void load() {
        List<String> raw = FileUtils.readAllLinesList(location.asStream());
        for(String line : raw) {
            if(line.startsWith("m")) material = AssetManager.getAsset(line.split(":")[1]);
            else if(line.startsWith("o")) {
                String[] e = line.split(":")[1].split(",");
                offset = new Rect(Float.parseFloat(e[0]),Float.parseFloat(e[1]),Float.parseFloat(e[2]),Float.parseFloat(e[3]));
            } else if(line.startsWith("p")) {
                String[] e = line.split(":")[1].split(",");
                padding = new Rect(Float.parseFloat(e[0]),Float.parseFloat(e[1]),Float.parseFloat(e[2]),Float.parseFloat(e[3]));
            } else if(line.startsWith("n")) s = line.split(":")[1];
            create();
        }
    }

    private void create() {
        if(offset != null && padding != null && s != null) {
            sprites.add(0, new Sprite(name+"/"+s, offset, padding, material));
            spriteMap.put(s, sprites.get(0));

            offset = null;
            s = null;
            padding = null;
        }
    }

    public Sprite getSprite(String s) { return spriteMap.get(s); }
}
