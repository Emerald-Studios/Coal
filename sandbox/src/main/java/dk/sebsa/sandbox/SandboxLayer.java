package dk.sebsa.sandbox;


import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.ecs.Entity;
import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.enums.PolygonMode;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.events.Layer;
import dk.sebsa.coal.graph.RenderPipeline;
import dk.sebsa.coal.graph.SpriteSheet;
import dk.sebsa.coal.graph.renderes.GUI;
import dk.sebsa.coal.graph.renderes.SpriteRenderer;
import dk.sebsa.coal.io.KeyPressedEvent;
import dk.sebsa.coal.physm.M2D.MAABBCollider2D;
import dk.sebsa.sandbox.elements.AdvancedTest;
import dk.sebsa.sandbox.elements.CloseMenu;
import org.lwjgl.glfw.GLFW;

/**
 * @author Sebsa
 *
 */
public class SandboxLayer extends Layer {
    private final Application application;
    private SpriteSheet sheet;
    public static LangStatic lang;
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
        sheet = AssetManager.getAsset("internal/sheets/BlackGUI.sht");
        //lang = LangStatic.genStatic((Language) AssetManager.getAsset("sandboxassets/local/en_us.lang"));
        lang = LangStatic.genStatic(AssetManager.getAsset("sandboxassets/local/da_dk.lang"));

        ee = new Entity("Object");
        SpriteRenderer sr = new SpriteRenderer(AssetManager.getAsset("sandboxassets/Asuna.spr"));
        sr.scale = 0.35f;
        sr.layer = 2;
        ee.transform.setPosition(480, 0, 0);
        ee.addComponent(sr);
        ee.addComponent(new MAABBCollider2D(sr));

        ee = new Entity("Object 2");
        sr = new SpriteRenderer(AssetManager.getAsset("sandboxassets/True.sht/Sega"));
        sr.scale = 0.8f;
        ee.transform.setPosition(160,160,0);
        ee.addComponent(new MAABBCollider2D(sr));
        ee.addComponent(sr);

        ee = new Entity("Player");
        sr = new SpriteRenderer(AssetManager.getAsset("sandboxassets/player.spr")); sr.scale = 0.34f;
        sr.layer = 1;
        ee.addComponent(sr);
        ee.addComponent(new Test2DMovement());
        ee.addComponent(new MAABBCollider2D(sr));

        ee = new Entity("Object 3 (Trigger)");
        ee.tag = "Door";
        sr = new SpriteRenderer(AssetManager.getAsset("sandboxassets/Door.spr"));
        sr.scale = 6f;
        ee.transform.setPosition(0,-190,0);
        ee.addComponent(new MAABBCollider2D(sr).setTrigger(true));
        ee.addComponent(sr);
    }

    @Override
    protected void update() {

    }

    @Override
    protected void cleanup() {

    }

    @Override
    protected SpriteSheet prefferedSpriteSheet() {
        return sheet;
    }


    @Override
    protected void buildUI() {
        Box();
        Box().pos(100,0).size(200,200);

        HList(() -> {
            customElement(new CloseMenu(getSate("1"), application));
            customElement(new CloseMenu(getSate("2"), application));
            customElement(new CloseMenu(getSate("3"), application));
            return null;
        }).pos(0,100);

        TextField(1, "Hello World!").prefix("{String} ").size(250, 20);
    }
}
