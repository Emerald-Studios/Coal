package dk.sebsa.coal.asset;

import dk.sebsa.coal.audio.Sound;
import dk.sebsa.coal.ecs.ComponentType;
import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.graph.*;
import dk.sebsa.coal.graph.text.Font;
import dk.sebsa.coal.graph.text.Language;
import dk.sebsa.coal.util.ConfigAsset;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author sebs
 * @since 1.0.0
 */
public class AssetManager {
    public static List<Asset> loadedAssets = new ArrayList<>();
    public static final List<Asset> newAssets = new ArrayList<>();
    private static final Class<AssetManager> clazz = AssetManager.class;
    private static final ClassLoader cl = clazz.getClassLoader();
    private static final List<AssetProvider> assetProviders = new ArrayList<>();
    private static final List<AssetLocation> assetLocations = new ArrayList<>();
    @Getter
    private static final Map<String, Asset> assetNameMap = new HashMap<>();

    public static <T extends Asset> T getAsset(String name) {
        return (T) assetNameMap.get(name);
    }

    public static void initGetAllAssets(Consumer<Object> log, Consumer<Object> errorLog) {
        log.accept("Getting list of all assets");

        // Load all assets from jar
        log.accept("Load from jar");
        URL dirUrl = cl.getResource("dk/sebsa/coal");
        String protocol = dirUrl.getProtocol();

        try { // Depending on the enviroment the assets has to be loaded from an "external folder" (Often when running from IDE)
            if(dirUrl != null && protocol.equals("file")) { log.accept("IDE Support Jar Load"); importFromSketchyJar(log, errorLog); }
            else { log.accept("Classic Jar Load"); importFromJar();}
        } catch (IOException e) { errorLog.accept("Error loading assets: "); e.printStackTrace(); }

        // Get assets from asset providers
        log.accept("Load from " + assetProviders.size() + " AssetProvider(s)");
        for(AssetProvider provider : assetProviders) {
            assetLocations.addAll(provider.getAssets());
        }
    }

    private static void importFromJar() throws IOException {
        // Loads the engine resources from a jar
        String me = clazz.getName().replace(".", "/") + ".class";
        URL dirUrl = cl.getResource(me);

        if(dirUrl.getProtocol().equals("jar")) {
            String jarPath = dirUrl.getPath().substring(5, dirUrl.getPath().indexOf("!"));
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8));
            Enumeration<JarEntry> entries = jar.entries();

            while(entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if(name.startsWith("coal/") && !name.endsWith("/")) assetLocations.add(new AssetLocation(AssetLocationType.Jar, "/" + name));
            }

            jar.close();
        }
    }

    private static void importFromSketchyJar(Consumer<Object> log, Consumer<Object> errorLog) throws IOException {
        List<String> paths = new ArrayList<>();
        List<String> streams = new ArrayList<>();
        streams.add("coal");

        while(!streams.isEmpty()) {
            InputStream in = cl.getResourceAsStream(streams.get(0));
            if(in == null) errorLog.accept("When importing assets from jar folder was not found: coal");

            assert in != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = br.readLine()) != null) {
                if(line.contains(".")) assetLocations.add(new AssetLocation(AssetLocationType.Jar, "/" + streams.get(0) + "/" + line));
                else streams.add(streams.get(0) + "/" + line);
            }

            in.close();
            br.close();
            streams.remove(0);
        }
    }

    public static void initCreateAllAssets(Consumer<Object> log, Consumer<Object> warnLog, Consumer<Object> errorLog) {
        log.accept("Instantiate all assets");
        // Instantiate all assets
        AssetLocation l = null;
        for(AssetLocation location : assetLocations) {
            try {
                l = location;
                if(location.location().endsWith(".coal")) newAssets.add(new ConfigAsset(location).name());
                else if(location.location().endsWith(".png")) newAssets.add(new Texture(location).name());
                else if(location.location().endsWith(".glsl")) newAssets.add(new GLSLShaderProgram(location).name());

                else if(location.location().endsWith(".mat")) newAssets.add(new Material(location).name());
                else if(location.location().endsWith(".spr")) newAssets.add(new Sprite(location).name());
                else if(location.location().endsWith(".sht")) newAssets.add(new SpriteSheet(location).name());

                else if(location.location().endsWith(".fnt")) newAssets.add(new Font(location).name());
                else if(location.location().endsWith(".lang")) newAssets.add(new Language(location).name());
                else if(location.location().endsWith(".ogg")) newAssets.add(new Sound(location).name());
                else if(location.location().endsWith(".java")) newAssets.add(new ComponentType(location).name());

                else errorLog.accept("Unknown asset type, " + location);
            } catch (AssetExitsException e) { warnLog.accept("Asset " + l + ", already exists"); }
        }
    }

    public static void initLoadAllAssets(Consumer<Object> log) {
        log.accept("Load all assets");
        try {
            Asset a;
            while (!newAssets.isEmpty()) {
                newAssets.get(0).loadAsset();
                newAssets.remove(0);
            }
        } catch (AssetExitsException e) { /* This won't happen since they have already been named */ }
    }

    public static void reset() {
        loadedAssets = new ArrayList<>();
    }

    public static void addAssetProvider(AssetProvider provider) {
        assetProviders.add(provider);
    }

    public static void removeAssetProvider(AssetProvider provider) {
        assetProviders.remove(provider);
    }
}
