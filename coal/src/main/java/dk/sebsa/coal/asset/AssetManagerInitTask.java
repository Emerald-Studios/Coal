package dk.sebsa.coal.asset;

import dk.sebsa.coal.tasks.Task;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

/**
 * @author sebs
 * @since 1.0.0
 */
public class AssetManagerInitTask extends Task {
    private final GLCapabilities glCapabilities;
    private final long windowId;

    public AssetManagerInitTask(long id, GLCapabilities capabilities) {
        this.windowId = id;
        this.glCapabilities = capabilities;
    }

    @Override
    protected String name() { return getClass().getSimpleName(); }

    @Override
    public void run() {
        GLFW.glfwMakeContextCurrent(windowId);
        GL.setCapabilities(glCapabilities);

        AssetManager.initGetAllAssets(this::log, this::error);
        AssetManager.initCreateAllAssets(this::log, this::error);
        AssetManager.initLoadAllAssets(this::log, this::warn);

        GL.setCapabilities(null);
        GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
    }
}
