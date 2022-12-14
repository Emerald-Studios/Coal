package dk.sebsa.coal.io;


import dk.sebsa.Coal;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.math.Color;
import lombok.Getter;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.function.Supplier;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author sebs
 * @since 1.0.0
 */
public class GLFWWindow {
    private long id;

    private String windowTitle;
    private Color clearColor;
    @Getter
    private int width, height;
    private final boolean vsync = false;
    private boolean minimized = false;
    @Getter
    private boolean isDirty;
    private boolean isFullscreen, actuallyFullscreen = false;
    private final int[] posX = new int[1];
    private final int[] posY = new int[1];
    @Getter
    private GLCapabilities glCapabilities;
    public final Rect rect = new Rect();
    public final Rect renderRect = new Rect();
    public Supplier<Rect> renderRectProvider;

    private void log(String s) { Coal.logger.log(s, "GLFWWindow"); }
    public GLFWWindow(String windowTitle, Color clearColor, int width, int height) {
        this(windowTitle, clearColor, width, height, null);
    }

    public GLFWWindow(String windowTitle, Color clearColor, int width, int height, Supplier<Rect> renderRectProvider) {
        this.windowTitle = windowTitle;
        this.clearColor = clearColor;
        this.width = width;
        this.height = height;
        this.isDirty = true;
        this.renderRectProvider = renderRectProvider;

        rect.set(0,0,width,height);
    }

    public void init() {
        log("Init GLFW");

        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // OSX Support
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);


        log("Create Window (" + windowTitle + ")");
        // Create the window
        id = glfwCreateWindow(260, 310, "Coal Application", NULL, NULL);
        if ( id == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        log("Setup resize callback");
        // Setup resize callback
        glfwSetFramebufferSizeCallback(id, (window, w, h) -> {
            isDirty = true;
            if(!actuallyFullscreen) {
                this.width = w;
                this.height = h;
                rect.set(0,0,width,height);
                if(renderRectProvider == null) renderRect.set(rect);
                else renderRect.set(renderRectProvider.get());
                log("RenderRect changed to: " + renderRect);
            }
            minimized = w == 0 && h == 0;

            glViewport(0, 0, w, h);
        });

        log("Push first frame..");
        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(id, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    id,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically


        log("Finalizing window setup 1");
        glfwMakeContextCurrent(id);
        glfwSwapInterval(vsync ? 1 : 0);

        // Anti alizin
        glfwWindowHint(GLFW_STENCIL_BITS, 4);
        glfwWindowHint(GLFW_SAMPLES, 4);

        log("Init OpenGL");
        // Init OpenGL
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        glCapabilities = GL.createCapabilities();

        log("Finalizing window setup 2");
        resetRenderRect();
        // Enable transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Culling
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Enable depth test
        glEnable(GL_DEPTH_TEST);
        glfwShowWindow(id);

        Coal.graphicsCard = glGetString(GL_RENDERER) + " " + glGetString(GL_VENDOR);
        log("Graphics Card: " + Coal.graphicsCard);
    }

    public void update() {
        // Changes stuff
        if(isFullscreen != actuallyFullscreen) {
            actuallyFullscreen = isFullscreen;
            log("Changed fullscren to: " + isFullscreen);

            if (isFullscreen) {
                GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                glfwGetWindowPos(id, posX, posY);
                glfwSetWindowMonitor(id, glfwGetPrimaryMonitor(), 0, 0, videoMode.width(), videoMode.height(), GLFW_DONT_CARE);
                // Enable v-sync
                glfwSwapInterval(vsync ? 1 : 0);
            } else glfwSetWindowMonitor(id, 0, posX[0], posY[0], width, height, GLFW_DONT_CARE);
        }
    }

    public void loadMode() {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glfwSetWindowTitle(id, "Loading...");
    }

    public void normalMode() {
        glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        glfwSetWindowSize(id, width, height);
        glfwSetWindowTitle(id, windowTitle);

        centerWindow();
    }

    public void cleanup() {
        log("Window Cleanup");
//		glfwFreeCallbacks(id);
        glfwDestroyWindow(id);

        log("Termionate GLFW");
        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void setSize(int width, int height) {
        this.width = width; this.height = height;
        if(renderRectProvider == null) renderRect.set(rect);
        else renderRect.set(renderRectProvider.get());

        glfwSetWindowSize(id, this.width, this.height);
        log("Changed size to: " + width + ", " + height);
        log("RenderRect size: " + renderRect);
    }

    public void resetRenderRect() {
        if(renderRectProvider == null) renderRect.set(rect);
        else renderRect.set(renderRectProvider.get());
        log("FORCE: RenderRect changed size: " + renderRect);
    }

    public void setClearColor(Color c) { clearColor = c; glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a); log("Changed clearcolor to: " + c); }
    public void setWindowTitle(String s) { windowTitle = s; glfwSetWindowTitle(id, s); log("Changed windowtitle to: " + s); }

    public void setFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
    }

    /**
     * @return the isFullscreen
     */
    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void centerWindow() {
        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Center the window
        assert vidmode != null;
        isDirty = true;
        glfwSetWindowPos(
                id,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );
    }

    /**
     * @return The GLFW window id
     */
    public long getID() {
        return id;
    }

    /**
     * @return the minimized
     */
    public boolean isMinimized() {
        return minimized;
    }

    public void endFrame() {
        if(isDirty && Coal.TRACE) log("Window was dirty");
        isDirty = false;
    }
}
