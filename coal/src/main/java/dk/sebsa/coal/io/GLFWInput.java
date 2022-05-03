package dk.sebsa.coal.io;


import dk.sebsa.coal.Application;
import dk.sebsa.coal.math.Vector2f;
import dk.sebsa.emerald.Logable;
import dk.sebsa.emerald.Logger;
import org.lwjgl.glfw.*;

/**
 * @author sebs
 * @since 1.0.0
 */
public class GLFWInput extends Logable {
    // Input Data
    private byte[] keys;
    private byte[] keysPressed;
    private byte[] keysReleased;
    private byte[] buttons;
    private byte[] buttonsPressed;
    private byte[] buttonsReleased;
    private double scrollX, scrollY = 0;
    private double mouseX, mouseY;
    private final Vector2f mousePos = new Vector2f(0, 0);

    // Input Class Stuff
    private final GLFWWindow window;

    // Callbacks
    private final GLFWMouseButtonCallback mouseButtonCallback;
    private final GLFWKeyCallback keyCallback;
    private final GLFWCursorPosCallback cursorCallback;
    private final GLFWScrollCallback scrollCallback;

    // Main Class
    public GLFWInput(Application app, Logger logger) {
        super(logger);
        this.window = app.window;
        resetInputData(true);

        log("Input init for " + app);

        // Create Callbacks
        mouseButtonCallback = new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (byte)(action != GLFW.GLFW_RELEASE?1:0);

                if(action == 1) {
                    buttonsPressed[button] = 1;

                    ButtonPressedEvent e = new ButtonPressedEvent();
                    e.button = button;
                    app.stack.event(e);
                } else if(action == 0) {
                    buttonsReleased[button] = 1;
                    ButtonReleasedEvent e = new ButtonReleasedEvent();
                    e.button = button;
                    app.stack.event(e);
                }
            }
        };

        keyCallback = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(key != -1) {
                    keys[key] = (byte)(action != GLFW.GLFW_RELEASE?1:0);
                    if(action == 1) {
                        keysPressed[key] = 1;
                        KeyPressedEvent e = new KeyPressedEvent();
                        e.key = key;
                        app.stack.event(e);
                    }
                    if(action == 0) {
                        keysReleased[key] = 1;
                        KeyReleasedEvent e = new KeyReleasedEvent();
                        e.key = key;
                        app.stack.event(e);
                    }
                }
            }
        };

        cursorCallback = new GLFWCursorPosCallback() {
            public void invoke(long window, double xpos, double ypos) {
                // Create Event
                MouseMoveEvent e = new MouseMoveEvent();
                e.mousePosX[0] = (int) xpos;
                e.mousePosY[0] = (int) ypos;
                e.offsetMousePosX[0] = (int) (mouseX-xpos);
                e.offsetMousePosY[0] = (int) (mouseY-ypos);
                app.stack.event(e);

                // Save Mousepos
                mouseX = xpos;
                mouseY = ypos;
                mousePos.set((float) mouseX, (float) mouseY);
            }
        };

        scrollCallback = new GLFWScrollCallback() {
            public void invoke(long window, double offsetx, double offsety) {
                MouseScrollEvent e = new MouseScrollEvent();
                e.offsetX = offsetx;
                e.offsetY = offsety;
                app.stack.event(e);

                scrollX += offsetx;
                scrollY += offsety;
            }
        };
    }

    public void addCallbacks() {
        log("Setting input callbacks");
        // Set callbacks
        GLFW.glfwSetKeyCallback(window.getID(), keyCallback);
        GLFW.glfwSetCursorPosCallback(window.getID(), cursorCallback);
        GLFW.glfwSetScrollCallback(window.getID(), scrollCallback);
        GLFW.glfwSetMouseButtonCallback(window.getID(), mouseButtonCallback);
//		GLFW.glfwSetCursorEnterCallback(window.id, cursorEnter);
//		GLFW.glfwSetCharCallback(window.id, text);
    }

    private void resetInputData(boolean all) {
        // Bool to byte
        if(all) keys = new byte[GLFW.GLFW_KEY_LAST];
        keysPressed = new byte[GLFW.GLFW_KEY_LAST];
        keysReleased = new byte[GLFW.GLFW_KEY_LAST];

        if(all) buttons = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
        buttonsPressed = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
        buttonsReleased = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
    }

    public void cleanup() {
        log("Freeing GLFW Input callbacks");
        mouseButtonCallback.free();
        keyCallback.free();
        cursorCallback.free();
        scrollCallback.free();
    }

    /**
     * Reset Button Pressed and KeyPressed
     */
    public void endFrame() {
        resetInputData(false);
    }

    // GET INPUT DATA METHODS
    // BORING AF
    // DONT LOOK AT
    public Vector2f getMousePos() { return mousePos; }

    public boolean isKeyDown(int key) {
        if(key < 0 || key > GLFW.GLFW_KEY_LAST) throw new IllegalArgumentException("Key not supported by GLFW");
        return keys[key] == 1;
    }

    public boolean isKeyPressed(int key) {
        if(key < 0 || key > GLFW.GLFW_KEY_LAST) throw new IllegalArgumentException("Key not supported by GLFW");
        return keysPressed[key] == 1;
    }

    public boolean isKeyReleased(int key) {
        if(key < 0 || key > GLFW.GLFW_KEY_LAST) throw new IllegalArgumentException("Key not supported by GLFW");
        return keysReleased[key] == 1;
    }

    public boolean isButtonDown(int button) {
        if(button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) throw new IllegalArgumentException("Button not supported by GLFW");
        return buttons[button] == 1;
    }

    public boolean isButtonPressed(int button) {
        if(button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) throw new IllegalArgumentException("Button not supported by GLFW");
        return buttonsPressed[button] == 1;
    }

    public boolean isButtonReleased(int button) {
        if(button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) throw new IllegalArgumentException("Button not supported by GLFW");
        return buttonsReleased[button] == 1;
    }

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }
}
