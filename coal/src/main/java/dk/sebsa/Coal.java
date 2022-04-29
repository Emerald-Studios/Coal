package dk.sebsa;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetExitsException;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.asset.AssetManagerInitTask;
import dk.sebsa.coal.debug.CoalImGUI;
import dk.sebsa.coal.debug.LogTracker;
import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.events.LayerStackEventTask;
import dk.sebsa.coal.events.LayerStackInitTask;
import dk.sebsa.coal.events.LayerStackUpdateTask;
import dk.sebsa.coal.graph.FBO;
import dk.sebsa.coal.graph.GLSLShaderProgram;
import dk.sebsa.coal.graph.renderes.Core2D;
import dk.sebsa.coal.math.Time;
import dk.sebsa.coal.tasks.TaskManager;
import dk.sebsa.coal.tasks.ThreadLogging;
import dk.sebsa.coal.tasks.ThreadManager;
import dk.sebsa.coal.util.InitScreenRenderer;
import dk.sebsa.emerald.Logable;
import dk.sebsa.emerald.Logger;
import dk.sebsa.emerald.LoggerFactory;
import dk.sebsa.emerald.outputs.FileOutput;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * The mother of all Coal programs
 *
 * @author sebs
 * @since 1.0.0
 */
public class Coal extends Logable {
    public static Coal instance;

    // Coal Settings & Info
    public static final String COAL_VERSION = "1.0.0-SNAPSHOT";
    public static boolean DEBUG = true;
    public static boolean TRACE = true;

    // Sys Info
    public static String graphicsCard = "Toaster";

    // Runtime stuff
    public static Logger logger;
    private Application application;
    private TaskManager taskManager;
    private ThreadManager threadManager;

    /**
     * Adds and application to the Coal runtime. (Currently coal doesn't support more than 1 running app)
     * If no coal instance exist, this will create it.
     *
     * @param app A new Coal application
     * @param debug Enables or Disables debug features
     */
    public static void fireUp(Application app, boolean debug) {
        if(instance != null) instance.addApplication(app);
        else {
            Coal.DEBUG = debug;
            Emerald.VERBOSE_LOAD = debug;

            // Logging
            LoggerFactory loggerFactory = new LoggerFactory();
            logger = loggerFactory.buildFromFile("/loggers/CoalEngineLogger.xml");
            Emerald.logSystemInfo(logger);
            if(DEBUG) logger.addOutput(new LogTracker());
            new Coal(app);
        }
    }


    private void addApplication(Application app) {
        if(application != null) throw new IllegalStateException("Coal currently doesn't support multiple applications");
        else this.application = app;

        log("Added application: " + app);
    }

    private Coal(Application app) {
        super(logger);
        instance = this;

        log("Running using Coal, " + COAL_VERSION);
        addApplication(app);

        // Main Coal stuff
        try {
            init();		// Init Coal, and setup workers (+Pre-Main Loop)
            mainLoop();	// Actual main loop
        } catch (Exception | Error e) { e.printStackTrace(); }

        FileOutput.flush();

        // Close Stuff
        log("Entering Cleanup");
        if(DEBUG) CoalImGUI.cleanup();
        application.cleanup();
        AssetManager.cleanup();
        FBO.cleanup();
        threadManager.stop();
        ThreadLogging.logAll(logger);
        log("I thinks this is it, my time has come. Bye!");
        log("(Program lifetime was: " + (Time.getTime() * 0.001f)/60 + " Minutes)");
        Emerald.close();
    }

    private void init() {
        Time.init();

        // Init GLFW and OpenGL
        log("Initializing GLFW & OpenGL");
        application.init();
        application.window.loadMode();

        // Debug only inits
        if(DEBUG) CoalImGUI.init(application);

        // Init Workers
        log("Initializing Threading Platform");
        taskManager = new TaskManager();
        threadManager = new ThreadManager(taskManager, logger);

        threadManager.init();

        // Add init tasks
        log("Adding init tasks to multi threaded worker system");
        taskManager.doTask(new AssetManagerInitTask(application.window.getID(), application.window.getGlCapabilities()));
        taskManager.doTask(new LayerStackInitTask(application.stack));

        // Core2D init
        log("Render Screen...");
        GLSLShaderProgram shader2d;
        try { shader2d = (GLSLShaderProgram) new GLSLShaderProgram(new AssetLocation(AssetLocationType.Jar, "/coal/internal/shaders/Coal2dDefault.glsl")).loadAsset(); }
        catch (AssetExitsException e) { shader2d = (GLSLShaderProgram) AssetManager.getAsset("internal/shaders/Coal2dDefault.glsl"); }
        Core2D.init(application.window, shader2d);

        // Render Init Screen, this is done once pr color buffer
        Core2D.prepare();
        InitScreenRenderer.render(application.window.rect);
        glfwSwapBuffers(application.window.getID());
        InitScreenRenderer.render(application.window.rect);
        Core2D.unprepare();
        log("Render Screen Done!");

        // Remove capabilities cause AssetManagerInit uses them
        GL.setCapabilities(null);
        glfwMakeContextCurrent(MemoryUtil.NULL);

        // Pre-Main Loop
        log("Entering pre-main loop");
        while(taskManager.stuffToDo()) {
            glfwSwapBuffers(application.window.getID()); // swap the color buffers
            glfwPollEvents(); // Poll for window events.

            // Make Stuff Happen
            ThreadLogging.logAll(logger);
            taskManager.frame(threadManager);
        }

        // Return Capabilities
        glfwMakeContextCurrent(application.window.getID());
        GL.setCapabilities(application.window.getGlCapabilities());

        // Pre-Main Loop
        application.window.normalMode();
        log("Exiting...");
    }

    public static void shutdownDueToError() {
        logger.log("A shutdown request had been created");
        instance.application.forceClose();
    }

    private void mainLoop() {
        log("Entering Main Loop");

        while(!application.shouldClose()) {
            FileOutput.flush();
            // Poll For Events
            glfwPollEvents();

            // Handle Logic (Frame Loop)
            // What needs to happen:
            // 	-X Handle events
            //	-O Window & Input Update
            //	-X LayerUpdate
            //	-X Update Time
            //	-O Update Entities
            Time.procsess();

            taskManager.doTask(new LayerStackUpdateTask(application.stack)); // Handle event task
            taskManager.doTask(new LayerStackEventTask(application.stack)); // Handle event task

            while(taskManager.stuffToDo()) {
                taskManager.frame(threadManager);
                ThreadLogging.logAll(logger);
            }

            application.window.update();

            // Render
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            if(application.renderPipeline != null) application.renderPipeline.renderStageAll(application);
            application.stack.render();                               // Render Layerstack UI

            glfwSwapBuffers(application.window.getID()); // swap the color buffers

            // Late Updates
            application.window.endFrame();
            application.input.endFrame();
        }
    }
}
