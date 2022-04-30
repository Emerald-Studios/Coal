package dk.sebsa.sandbox;


import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.enums.PolygonMode;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.RenderPipeline;
import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.graph.text.Font;
import dk.sebsa.coal.graph.text.Label;
import dk.sebsa.coal.io.KeyPressedEvent;
import dk.sebsa.coal.math.Color;
import org.lwjgl.glfw.GLFW;

/**
 * @author Sebsa
 *
 */
public class SandboxLayer extends Layer {
    private final Application application;
    private SpriteSheet sheet;
    private Label label, cum;
    private static final Rect r1 = new Rect(0,200,200,200);
    private static final Rect r2 = new Rect(0,400,400,42);
    private static final Rect r3 = new Rect(0,442,50,42);

    public SandboxLayer(Application app) {
        this.application = app;
    }

    @Override
    public boolean handleEvent(Event e) {
        if(e.eventType().equals(EventTypes.KeyPressed)) {
            KeyPressedEvent e2 = (KeyPressedEvent) e;
            if(e2.key == GLFW.GLFW_KEY_F11) application.window.setFullscreen(!application.window.isFullscreen());
        }
        return false;
    }

    @Override
    protected void init() {

    }

    private void enit() {
        sheet = (SpriteSheet) AssetManager.getAsset("internal/sheets/BlackGUI.sht");

        Font font = (Font) AssetManager.getAsset("sandboxassets/Test.fnt");
        label = new Label("U wanna...", font, Color.white);
        cum = new Label("Cum in me", font, Color.color(1,1,1,0.025f));
    }

    @Override
    protected void update() {
        if(application.input.isKeyPressed(GLFW.GLFW_KEY_F4)) {
            RenderPipeline rp = application.renderPipeline;
            rp.polygonMode = (rp.polygonMode == PolygonMode.Fill) ? PolygonMode.Line : PolygonMode.Fill;
        }
        if(application.input.isKeyPressed(GLFW.GLFW_KEY_F3)) Sandbox.instance.debugLayer.enabled = !Sandbox.instance.debugLayer.enabled;
    }

    @Override
    protected void render() {
        if(sheet == null) enit();
        GUI.prepare(sheet, application);
        GUI.box(r1);
        if(GUI.buttonDown(r2, label))  GUI.label(r3, cum);
        GUI.unprepare();
    }

    @Override
    protected void cleanup() {

    }
}
