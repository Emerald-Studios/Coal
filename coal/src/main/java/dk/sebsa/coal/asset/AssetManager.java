package dk.sebsa.coal.asset;

import dk.sebsa.Coal;
import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.graph.GLSLShaderProgram;
import dk.sebsa.coal.graph.Texture;
import dk.sebsa.coal.util.ConfigAsset;
import lombok.Getter;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AssetManager {
    public static List<Asset> loadedAssets = new ArrayList<>();
    public static List<Asset> newAssets = new ArrayList<>();
    private static final Class<AssetManager> clazz = AssetManager.class;
    private static final ClassLoader cl = clazz.getClassLoader();
    private static final List<AssetProvider> assetProviders = new ArrayList<>();
    private static final List<AssetLocation> assetLocations = new ArrayList<>();
    @Getter
    private static final Map<String, Asset> assetNameMap = new HashMap<>();

    public static Asset getAsset(String name) {
        return assetNameMap.get(name);
    }

    public static void initGetAllAssets(Consumer<Object> log) {
        log.accept("Getting list of all assets");

        // Load all assets from jar
        log.accept("Load from jar");
        URL dirUrl = cl.getResource("dk/sebsa/coal");
        String protocol = dirUrl.getProtocol();

        try { // Depending on the enviroment the assets has to be loaded from an "external folder" (Often when running from IDE)
            if(dirUrl != null && protocol.equals("file")) { log.accept("IDE Support Jar Load"); importFromSketchyJar(log); }
            else { log.accept("Classic Jar Load"); importFromJar();}
        } catch (IOException e) { log.accept("Error loading assets: "); e.printStackTrace(); }

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

    private static void importFromSketchyJar(Consumer<Object> log) throws IOException {
        List<String> paths = new ArrayList<>();
        InputStream in = cl.getResourceAsStream("coal");
        if(in == null) {
            log.accept("When importing assets from jar folder was not found: coal");
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = br.readLine()) != null) {
                assetLocations.add(new AssetLocation(AssetLocationType.Jar, "/" + line));
            }

            in.close();
            br.close();
        }
    }

    public static void initCreateAllAssets(Consumer<Object> log) {
        log.accept("Instantiate all assets");
        // Instantiate all assets
        for(AssetLocation location : assetLocations) {
            if(location.location().endsWith(".coal")) newAssets.add(new ConfigAsset(location));
            else if(location.location().endsWith(".glsl")) newAssets.add(new GLSLShaderProgram(location));
            else if(location.location().endsWith(".png")) newAssets.add(new Texture(location));
            else log.accept("Unknown asset type, " + location);
        }
    }

    public static void initLoadAllAssets() {
        Coal.logger.log("Load all assets", clazz.getSimpleName());

        Asset a;
        while (!newAssets.isEmpty()) {
            a = newAssets.get(0);
            try {
                a.loadAsset();
            } catch (AssetExitsException e) { Coal.logger.log("Asset " + a.name + ", already exists", clazz.getSimpleName()); }

            newAssets.remove(0);
        }
    }
    
    public static void cleanup() {
        for (Asset a: loadedAssets) {
            a.destroy();
        }

        loadedAssets = new ArrayList<>();
    }
}
