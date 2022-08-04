package dk.sebsa.coal.ecs;

import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.util.ClassImporter;

import java.io.IOException;

/**
 * @author sebs
 */
public class ComponentType extends Asset {
    public ComponentType(AssetLocation location) {
        super(location);
    }

    @Override
    protected void load() {
        // TODO MAKE THIS
        /*ClassImporter<Component> classImporter = new ClassImporter<>();
        try {
            classImporter.importClass(location, Component.class);
        } catch (IOException e) {
            log("IOException while trying to import component: " + location + ", FAILED");
        }*/
    }

    @Override
    public void destroy() {

    }
}
