package dk.sebsa.sandbox;


import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.audio.AudioManager;
import dk.sebsa.coal.audio.Sound;
import dk.sebsa.coal.ecs.Entity;
import dk.sebsa.coal.ecs.collision.BoxCollider2D;
import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.enums.PolygonMode;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.graph.RenderPipeline;
import dk.sebsa.coal.graph.Sprite;
import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;
import dk.sebsa.coal.graph.text.Font;
import dk.sebsa.coal.graph.text.Label;
import dk.sebsa.coal.graph.text.Language;
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
    private Label label, cum, soundsLabel;
    private Sound susan, anglerfish, ender;
    private Font font;
    private boolean sounds;
    public static LangStatic lang;
    private static final Rect r1 = new Rect(0,200,200,200);
    private static final Rect r2 = new Rect(0,400,400,42);
    private static final Rect r3 = new Rect(0,442,50,42);
    private static final Rect r4 = new Rect(0,484,400,42);
    private Entity ee;

    public SandboxLayer(Application app) {
        this.application = app;
    }

    @Override
    public boolean handleEvent(Event e) {
        if(e.eventType().equals(EventTypes.KeyPressed)) {
            KeyPressedEvent e2 = (KeyPressedEvent) e;
            if(e2.key == GLFW.GLFW_KEY_F11) application.window.setFullscreen(!application.window.isFullscreen());
            else if(e2.key == GLFW.GLFW_KEY_F3) Sandbox.instance.debugLayer.enabled = !Sandbox.instance.debugLayer.enabled;
            else if(e2.key == GLFW.GLFW_KEY_F4) {
                RenderPipeline rp = application.renderPipeline;
                rp.polygonMode = (rp.polygonMode == PolygonMode.Fill) ? PolygonMode.Line : PolygonMode.Fill;
            } else if(e2.key == GLFW.GLFW_KEY_F2) Sandbox.instance.debugRenderStage.setEnabled(!Sandbox.instance.debugRenderStage.isEnabled());
        }
        return false;
    }

    @Override
    protected void init() {
        log("Huston fuck me");
        sheet = (SpriteSheet) AssetManager.getAsset("internal/sheets/BlackGUI.sht");
        //lang = LangStatic.genStatic((Language) AssetManager.getAsset("sandboxassets/local/en_us.lang"));
        lang = LangStatic.genStatic((Language) AssetManager.getAsset("sandboxassets/local/da_dk.lang"));
        susan = (Sound) AssetManager.getAsset("sandboxassets/Susan.ogg");
        ender = (Sound) AssetManager.getAsset("sandboxassets/Ender.ogg");
        anglerfish = (Sound) AssetManager.getAsset("sandboxassets/AnglerFish.ogg");

        font = (Font) AssetManager.getAsset("sandboxassets/Test.fnt");
        label = new Label(lang.sandboxTest1, font, Color.white);
        soundsLabel = new Label(lang.sandboxTest3, font, Color.white);
        cum = new Label(lang.sandboxTest2, font, Color.color(1,1,1,0.025f));

        ee = new Entity();
        SpriteRenderer sr = new SpriteRenderer((Sprite) AssetManager.getAsset("sandboxassets/True.sht/Sega"));
        ee.addComponent(sr);
        ee.addComponent(new Test2DMovement());
        ee.addComponent(new BoxCollider2D(sr));
    }

    @Override
    protected void update() {

    }

    @Override
    protected void render() {
        GUI.prepare(sheet, application);
        GUI.box(r1);
        if(GUI.buttonDown(r2, label))  GUI.label(r3, cum);

        // Enable Audio
        if(GUI.button(r4, soundsLabel)) sounds = !sounds;
        if(sounds) {
            if(GUI.button(new Rect(400, 484, 400, 42), new Label("Fish", font, Color.white))) AudioManager.playSound(anglerfish, 1);
            if(GUI.button(new Rect(400, 526, 400, 42), new Label("Susan", font, Color.white))) AudioManager.playSound(susan, 1);
        }

        GUI.unprepare();
    }

    @Override
    protected void cleanup() {

    }
}
