package dk.sebsa;

import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetExitsException;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.asset.AssetManagerInitTask;
import dk.sebsa.coal.audio.AudioManager;
import dk.sebsa.coal.audio.AudioUpdateTask;
import dk.sebsa.coal.debug.CoalImGUI;
import dk.sebsa.coal.debug.LogTracker;
import dk.sebsa.coal.ecs.ECSUpdateTask;
import dk.sebsa.coal.ecs.Entity;
import dk.sebsa.coal.enums.AssetLocationType;
import dk.sebsa.coal.events.LayerStackEventTask;
import dk.sebsa.coal.events.LayerStackUpdateTask;
import dk.sebsa.coal.graph.GLSLShaderProgram;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.renderes.Core2D;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;
import dk.sebsa.coal.math.Time;
import dk.sebsa.coal.tasks.TaskManager;
import dk.sebsa.coal.tasks.ThreadLogging;
import dk.sebsa.coal.tasks.ThreadManager;
import dk.sebsa.coal.trash.TCFrameTask;
import dk.sebsa.coal.trash.TrashCollector;
import dk.sebsa.coal.util.InitScreenRenderer;
import dk.sebsa.emerald.Logable;
import dk.sebsa.emerald.Logger;
import dk.sebsa.emerald.LoggerFactory;
import dk.sebsa.emerald.outputs.FileOutput;
import lombok.Getter;
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
    public static final String COAL_VERSION = "1.1.6";
    public static boolean DEBUG;
    public static boolean TRACE;

    // Sys Info
    public static String graphicsCard = "Toaster";

    // Runtime stuff
    public static Logger logger;
    @Getter private Application application;
    @Getter private TaskManager taskManager;
    @Getter private ThreadManager threadManager;
    @Getter private static CoalCapabilities capabilities;

    /**
     * Adds and application to the Coal runtime. (Currently coal doesn't support more than 1 running app)
     * If no coal instance exist, this will create it.
     *
     * @param app A new Coal application
     */
    public static void fireUp(Application app, CoalCapabilities caps) {
        if(instance != null) instance.addApplication(app);
        else {
            capabilities = caps;

            Coal.DEBUG = caps.coalDebug;
            Coal.TRACE = caps.coalTrace;
            Emerald.VERBOSE_LOAD = caps.coalTrace;

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
        TrashCollector.deepForceClean();
        try { AudioManager.cleanup(); } catch(Exception | Error e) { log("AudioManager cleanup failed"); }
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
        AudioManager.init();

        // Add init tasks
        log("Adding init tasks to multi threaded worker system");
        taskManager.doTask(new AssetManagerInitTask(application.window.getID(), application.window.getGlCapabilities()));
        // This is now done after the AssetManger - taskManager.doTask(new LayerStackInitTask(application.stack));

        // Core2D init
        GLSLShaderProgram shader2d;
        try { shader2d = (GLSLShaderProgram) new GLSLShaderProgram(new AssetLocation(AssetLocationType.Jar, "/coal/internal/shaders/Coal2dDefault.glsl")).loadAsset(); }
        catch (AssetExitsException e) { shader2d = AssetManager.getAsset("internal/shaders/Coal2dDefault.glsl"); }
        Core2D.init(application.window, shader2d);

        if(capabilities.coalLoadScreen) {
            log("Render Screen...");

            // Calculate image size from windows size
            Rect r = new Rect(application.window.rect);
            r.add(5,5,-10,-10);

            // Render Init Screen, this is done once pr color buffer
            Core2D.prepare();
            InitScreenRenderer.render(r);
            glfwSwapBuffers(application.window.getID());
            InitScreenRenderer.render(r);
            Core2D.unprepare();
            log("Render Screen Done!");
        }
        if(capabilities.coalSprite2D) SpriteRenderer.initDefaultShader();

        // Remove capabilities cause AssetManagerInit uses them
        GL.setCapabilities(null);
        glfwMakeContextCurrent(MemoryUtil.NULL);

        // Pre-Main Loop
        log("Entering pre-main loop");
        while(taskManager.stuffToDo()) {
            if(capabilities.coalLoadScreen) {
                glfwSwapBuffers(application.window.getID()); // swap the color buffers
                glfwPollEvents(); // Poll for window events.
            }

            // Make Stuff Happen
            ThreadLogging.logAll(logger);
            taskManager.frame(threadManager, true);
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
        if(capabilities.ignoreErrorShutdown) return;
        instance.application.forceClose();
    }

    private void mainLoop() {
        log("Entering Main Loop");

        while(!application.shouldClose()) {
            if(!capabilities.disableContinuousLogFlush) FileOutput.flush();
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
            taskManager.doTask(new ECSUpdateTask(Entity.master, application));
            if(capabilities.coalAudio) taskManager.doTask(new AudioUpdateTask()); // Handle event task
            if(capabilities.tcAudioClean) taskManager.doTask(new TCFrameTask()); // Handle event task

            while(taskManager.stuffToDo()) {
                taskManager.frame(threadManager, false);
                ThreadLogging.logAll(logger);
            }

            application.window.update();

            // Render
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            if(application.renderPipeline != null) application.renderPipeline.renderStageAll(application);
            application.stack.render();                               // Render LayerStack UI

            glfwSwapBuffers(application.window.getID()); // swap the color buffers

            // Late Updates
            application.window.endFrame();
            application.input.endFrame();

            // Empty Garbage Can
            while(!TrashCollector.literalTrash.isEmpty()) {
                TrashCollector.literalTrash.get(0).destroy();
                TrashCollector.trash.remove(TrashCollector.literalTrash.get(0));
                TrashCollector.literalTrash.remove(0);
            }
        }
    }
}
