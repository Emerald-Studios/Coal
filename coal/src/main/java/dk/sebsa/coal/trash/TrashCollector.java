package dk.sebsa.coal.trash;

import dk.sebsa.coal.asset.AssetManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebs
 */
public class TrashCollector {
    public static List<Trash> trash = new ArrayList<>(); // All trash instances
    public static List<Trash> literalTrash = new ArrayList<>(); // Literal Trash

    public static void trash(Trash t) {
        literalTrash.add(t);
    }

    public static void deepForceClean() {
        for(Trash trash : trash) {
            trash.destroy();
        } trash = new ArrayList<>();
        AssetManager.reset();
    }
}
