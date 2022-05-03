package dk.sebsa.coal.asset;

import dk.sebsa.Coal;
import dk.sebsa.coal.events.LayerStackInitTask;
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

    private void trace(Object o) { if(Coal.TRACE) log(o); }

    @Override
    public void run() {
        trace("Steal Context, GLFW");
        GLFW.glfwMakeContextCurrent(windowId);
        trace("Steal Context, GL");
        GL.setCapabilities(glCapabilities);

        AssetManager.initGetAllAssets(this::log, this::error);
        AssetManager.initCreateAllAssets(this::log, this::warn, this::error);
        AssetManager.initLoadAllAssets(this::log);

        trace("Return Contexts");
        GL.setCapabilities(null);
        GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);

        trace("Add LayerStackInitTask");
        Coal.instance.getTaskManager().doTask(new LayerStackInitTask(Coal.instance.getApplication().stack));
    }
}
