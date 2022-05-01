package dk.sebsa.coal.graph.text;

import dk.sebsa.Coal;
import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.Map;

/**
 * @author sebs
 */
public class Language extends Asset {
    private Map<String, Object> map;

    public Language(AssetLocation location) {
        super(location);
    }

    @Override
    public void destroy() {

    }

    public String get(String key) { return (String) map.get(key); }

    @Override
    protected void load() {
        InputStream inputStream = location.asStream();
        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject object = new JSONObject(tokener);
        map = object.toMap();
        if(Coal.TRACE) log("Loaded " + map.keySet().size() + " entries");
    }
}
