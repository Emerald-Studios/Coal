package dk.sebsa.coal.debug;


import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiPlatformIO;
import imgui.ImGuiViewport;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiNavInput;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWVidMode;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class CoalImGUIGlfwImpl {
    private static final String OS = System.getProperty("os.name", "generic").toLowerCase();
    protected static final boolean IS_WINDOWS = OS.contains("win");
    protected static final boolean IS_APPLE = OS.contains("mac") || OS.contains("darwin");

    // Pointer of the current GLFW window
    private long windowPtr;

    // Some features may be available only from a specific version
//    private boolean glfwHawWindowTopmost;
//    private boolean glfwHasWindowAlpha;
    private boolean glfwHasPerMonitorDpi;
    //    private boolean glfwHasFocusWindow;
//    private boolean glfwHasFocusOnShow;
    private boolean glfwHasMonitorWorkArea;
//    private boolean glfwHasOsxWindowPosFix;

    // For application window properties
    private final int[] winWidth = new int[1];
    private final int[] winHeight = new int[1];
    private final int[] fbWidth = new int[1];
    private final int[] fbHeight = new int[1];


    // Empty array to fill ImGuiIO.NavInputs with zeroes
    private final float[] emptyNavInputs = new float[ImGuiNavInput.COUNT];

    // Monitor properties
    private final int[] monitorX = new int[1];
    private final int[] monitorY = new int[1];
    private final int[] monitorWorkAreaX = new int[1];
    private final int[] monitorWorkAreaY = new int[1];
    private final int[] monitorWorkAreaWidth = new int[1];
    private final int[] monitorWorkAreaHeight = new int[1];
    private final float[] monitorContentScaleX = new float[1];
    private final float[] monitorContentScaleY = new float[1];

    // Internal data
    private boolean wantUpdateMonitors = true;
    private double time = 0.0;

    public boolean init(final long windowId, final boolean installCallbacks) {
        this.windowPtr = windowId;

        detectGlfwVersionAndEnabledFeatures();

        final ImGuiIO io = ImGui.getIO();

        io.addBackendFlags(ImGuiBackendFlags.HasMouseCursors | ImGuiBackendFlags.HasSetMousePos | ImGuiBackendFlags.PlatformHasViewports);
        io.setBackendPlatformName("imgui_java_impl_glfw");

        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;

        io.setKeyMap(keyMap);

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(windowId);
                return clipboardString != null ? clipboardString : "";
            }
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String str) {
                glfwSetClipboardString(windowId, str);
            }
        });


        // Update monitors the first time (note: monitor callback are broken in GLFW 3.2 and earlier, see github.com/glfw/glfw/issues/784)
        updateMonitors();

        // Our mouse update function expect PlatformHandle to be filled for the main viewport
        final ImGuiViewport mainViewport = ImGui.getMainViewport();
        mainViewport.setPlatformHandle(windowPtr);

        if (IS_WINDOWS) {
            mainViewport.setPlatformHandleRaw(GLFWNativeWin32.glfwGetWin32Window(windowId));
        }

        return true;
    }

    /**
     * Updates {@link ImGuiIO} and {@link org.lwjgl.glfw.GLFW} state.
     */
    public void newFrame() {
        final ImGuiIO io = ImGui.getIO();

        glfwGetWindowSize(windowPtr, winWidth, winHeight);
        glfwGetFramebufferSize(windowPtr, fbWidth, fbHeight);

        io.setDisplaySize((float) winWidth[0], (float) winHeight[0]);
        if (winWidth[0] > 0 && winHeight[0] > 0) {
            final float scaleX = (float) fbWidth[0] / winWidth[0];
            final float scaleY = (float) fbHeight[0] / winHeight[0];
            io.setDisplayFramebufferScale(scaleX, scaleY);
        }
        if (wantUpdateMonitors) {
            updateMonitors();
        }

        final double currentTime = glfwGetTime();
        io.setDeltaTime(time > 0.0 ? (float) (currentTime - time) : 1.0f / 60.0f);
        time = currentTime;

        updateGamepads();
    }

    /**
     */
    public void dispose() {

    }

    private void detectGlfwVersionAndEnabledFeatures() {
        final int[] major = new int[1];
        final int[] minor = new int[1];
        final int[] rev = new int[1];
        glfwGetVersion(major, minor, rev);

        final int version = major[0] * 1000 + minor[0] * 100 + rev[0] * 10;

//        glfwHawWindowTopmost = version >= 3200;
//        glfwHasWindowAlpha = version >= 3300;
        glfwHasPerMonitorDpi = version >= 3300;
//        glfwHasFocusWindow = version >= 3200;
//        glfwHasFocusOnShow = version >= 3300;
        glfwHasMonitorWorkArea = version >= 3300;
    }

    private void updateGamepads() {
        final ImGuiIO io = ImGui.getIO();

        if (!io.hasConfigFlags(ImGuiConfigFlags.NavEnableGamepad)) {
            return;
        }

        io.setNavInputs(emptyNavInputs);

        final ByteBuffer buttons = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
        final int buttonsCount = buttons.limit();

        final FloatBuffer axis = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        final int axisCount = axis.limit();

        mapButton(ImGuiNavInput.Activate, 0, buttons, buttonsCount, io);   // Cross / A
        mapButton(ImGuiNavInput.Cancel, 1, buttons, buttonsCount, io);     // Circle / B
        mapButton(ImGuiNavInput.Menu, 2, buttons, buttonsCount, io);       // Square / X
        mapButton(ImGuiNavInput.Input, 3, buttons, buttonsCount, io);      // Triangle / Y
        mapButton(ImGuiNavInput.DpadLeft, 13, buttons, buttonsCount, io);  // D-Pad Left
        mapButton(ImGuiNavInput.DpadRight, 11, buttons, buttonsCount, io); // D-Pad Right
        mapButton(ImGuiNavInput.DpadUp, 10, buttons, buttonsCount, io);    // D-Pad Up
        mapButton(ImGuiNavInput.DpadDown, 12, buttons, buttonsCount, io);  // D-Pad Down
        mapButton(ImGuiNavInput.FocusPrev, 4, buttons, buttonsCount, io);  // L1 / LB
        mapButton(ImGuiNavInput.FocusNext, 5, buttons, buttonsCount, io);  // R1 / RB
        mapButton(ImGuiNavInput.TweakSlow, 4, buttons, buttonsCount, io);  // L1 / LB
        mapButton(ImGuiNavInput.TweakFast, 5, buttons, buttonsCount, io);  // R1 / RB
        mapAnalog(ImGuiNavInput.LStickLeft, 0, -0.3f, -0.9f, axis, axisCount, io);
        mapAnalog(ImGuiNavInput.LStickRight, 0, +0.3f, +0.9f, axis, axisCount, io);
        mapAnalog(ImGuiNavInput.LStickUp, 1, +0.3f, +0.9f, axis, axisCount, io);
        mapAnalog(ImGuiNavInput.LStickDown, 1, -0.3f, -0.9f, axis, axisCount, io);

        if (axisCount > 0 && buttonsCount > 0) {
            io.addBackendFlags(ImGuiBackendFlags.HasGamepad);
        } else {
            io.removeBackendFlags(ImGuiBackendFlags.HasGamepad);
        }
    }

    private void mapButton(final int navNo, final int buttonNo, final ByteBuffer buttons, final int buttonsCount, final ImGuiIO io) {
        if (buttonsCount > buttonNo && buttons.get(buttonNo) == GLFW_PRESS) {
            io.setNavInputs(navNo, 1.0f);
        }
    }

    private void mapAnalog(
            final int navNo,
            final int axisNo,
            final float v0,
            final float v1,
            final FloatBuffer axis,
            final int axisCount,
            final ImGuiIO io
    ) {
        float v = axisCount > axisNo ? axis.get(axisNo) : v0;
        v = (v - v0) / (v1 - v0);
        if (v > 1.0f) {
            v = 1.0f;
        }
        if (io.getNavInputs(navNo) < v) {
            io.setNavInputs(navNo, v);
        }
    }

    private void updateMonitors() {
        final ImGuiPlatformIO platformIO = ImGui.getPlatformIO();
        final PointerBuffer monitors = glfwGetMonitors();

        platformIO.resizeMonitors(0);

        for (int n = 0; n < monitors.limit(); n++) {
            final long monitor = monitors.get(n);

            glfwGetMonitorPos(monitor, monitorX, monitorY);
            final GLFWVidMode vidMode = glfwGetVideoMode(monitor);
            final float mainPosX = monitorX[0];
            final float mainPosY = monitorY[0];
            final float mainSizeX = vidMode.width();
            final float mainSizeY = vidMode.height();

            if (glfwHasMonitorWorkArea) {
                glfwGetMonitorWorkarea(monitor, monitorWorkAreaX, monitorWorkAreaY, monitorWorkAreaWidth, monitorWorkAreaHeight);
            }

            float workPosX = 0;
            float workPosY = 0;
            float workSizeX = 0;
            float workSizeY = 0;

            // Workaround a small GLFW issue reporting zero on monitor changes: https://github.com/glfw/glfw/pull/1761
            if (glfwHasMonitorWorkArea && monitorWorkAreaWidth[0] > 0 && monitorWorkAreaHeight[0] > 0) {
                workPosX = monitorWorkAreaX[0];
                workPosY = monitorWorkAreaY[0];
                workSizeX = monitorWorkAreaWidth[0];
                workSizeY = monitorWorkAreaHeight[0];
            }

            // Warning: the validity of monitor DPI information on Windows depends on the application DPI awareness settings,
            // which generally needs to be set in the manifest or at runtime.
            if (glfwHasPerMonitorDpi) {
                glfwGetMonitorContentScale(monitor, monitorContentScaleX, monitorContentScaleY);
            }
            final float dpiScale = monitorContentScaleX[0];

            platformIO.pushMonitors(mainPosX, mainPosY, mainSizeX, mainSizeY, workPosX, workPosY, workSizeX, workSizeY, dpiScale);
        }

        wantUpdateMonitors = false;
    }
//
//    //--------------------------------------------------------------------------------------------------------
//    // MULTI-VIEWPORT / PLATFORM INTERFACE SUPPORT
//    // This is an _advanced_ and _optional_ feature, allowing the back-end to create and handle multiple viewports simultaneously.
//    // If you are new to dear imgui or creating a new binding for dear imgui, it is recommended that you completely ignore this section first..
//    //--------------------------------------------------------------------------------------------------------
//    private final class CreateWindowFunction extends ImPlatformFuncViewport {
//        @Override
//        public void accept(final ImGuiViewport vp) {
//            final ImGuiViewportDataGlfw data = new ImGuiViewportDataGlfw();
//
//            vp.setPlatformUserData(data);
//
//            // GLFW 3.2 unfortunately always set focus on glfwCreateWindow() if GLFW_VISIBLE is set, regardless of GLFW_FOCUSED
//            // With GLFW 3.3, the hint GLFW_FOCUS_ON_SHOW fixes this problem
//            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
//            glfwWindowHint(GLFW_FOCUSED, GLFW_FALSE);
//            if (glfwHasFocusOnShow) {
//                glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_FALSE);
//            }
//            glfwWindowHint(GLFW_DECORATED, vp.hasFlags(ImGuiViewportFlags.NoDecoration) ? GLFW_FALSE : GLFW_TRUE);
//            if (glfwHawWindowTopmost) {
//                glfwWindowHint(GLFW_FLOATING, vp.hasFlags(ImGuiViewportFlags.TopMost) ? GLFW_TRUE : GLFW_FALSE);
//            }
//
//            data.window = glfwCreateWindow((int) vp.getSizeX(), (int) vp.getSizeY(), "No Title Yet", NULL, windowPtr);
//            data.windowOwned = true;
//
//            vp.setPlatformHandle(data.window);
//
//            if (IS_WINDOWS) {
//                vp.setPlatformHandleRaw(GLFWNativeWin32.glfwGetWin32Window(data.window));
//            }
//
//            glfwSetWindowPos(data.window, (int) vp.getPosX(), (int) vp.getPosY());
//
//            // Install GLFW callbacks for secondary viewports
//
//            glfwMakeContextCurrent(data.window);
//            glfwSwapInterval(0);
//        }
//    }
//
//    /*private final class DestroyWindowFunction extends ImPlatformFuncViewport {
//        @Override
//        public void accept(final ImGuiViewport vp) {
//            vp.setPlatformUserData(null);
//            vp.setPlatformHandle(0);
//        }
//    }
//
//    private static final class ShowWindowFunction extends ImPlatformFuncViewport {
//        @Override
//        public void accept(final ImGuiViewport vp) {
//            final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//
//            if (IS_WINDOWS && vp.hasFlags(ImGuiViewportFlags.NoTaskBarIcon)) {
//                ImGuiImplGlfwNative.win32hideFromTaskBar(vp.getPlatformHandleRaw());
//            }
//
//            glfwShowWindow(data.window);
//        }
//    }
//
//    private static final class GetWindowPosFunction extends ImPlatformFuncViewportSuppImVec2 {
//        private final int[] posX = new int[1];
//        private final int[] posY = new int[1];
//
//        @Override
//        public void get(final ImGuiViewport vp, final ImVec2 dstImVec2) {
//            final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//            glfwGetWindowPos(data.window, posX, posY);
//            dstImVec2.x = posX[0];
//            dstImVec2.y = posY[0];
//        }
//    }
//
//    private static final class SetWindowPosFunction extends ImPlatformFuncViewportImVec2 {
//        @Override
//        public void accept(final ImGuiViewport vp, final ImVec2 imVec2) {
//            final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//            data.ignoreWindowPosEventFrame = ImGui.getFrameCount();
//            glfwSetWindowPos(data.window, (int) imVec2.x, (int) imVec2.y);
//        }
//    }
//
//    private static final class GetWindowSizeFunction extends ImPlatformFuncViewportSuppImVec2 {
//        private final int[] width = new int[1];
//        private final int[] height = new int[1];
//
//        @Override
//        public void get(final ImGuiViewport vp, final ImVec2 dstImVec2) {
//            final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//            glfwGetWindowSize(data.window, width, height);
//            dstImVec2.x = width[0];
//            dstImVec2.y = height[0];
//        }
//    }
//
//    private final class SetWindowSizeFunction extends ImPlatformFuncViewportImVec2 {
//        private final int[] x = new int[1];
//        private final int[] y = new int[1];
//        private final int[] width = new int[1];
//        private final int[] height = new int[1];
//
//        @Override
//        public void accept(final ImGuiViewport vp, final ImVec2 imVec2) {
//            final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//            // Native OS windows are positioned from the bottom-left corner on macOS, whereas on other platforms they are
//            // positioned from the upper-left corner. GLFW makes an effort to convert macOS style coordinates, however it
//            // doesn't handle it when changing size. We are manually moving the window in order for changes of size to be based
//            // on the upper-left corner.
//            if (IS_APPLE && !glfwHasOsxWindowPosFix) {
//                glfwGetWindowPos(data.window, x, y);
//                glfwGetWindowSize(data.window, width, height);
//                glfwSetWindowPos(data.window, x[0], y[0] - height[0] + (int) imVec2.y);
//            }
//            data.ignoreWindowSizeEventFrame = ImGui.getFrameCount();
//            glfwSetWindowSize(data.window, (int) imVec2.x, (int) imVec2.y);
//        }
//    }
//
//    private static final class SetWindowTitleFunction extends ImPlatformFuncViewportString {
//        @Override
//        public void accept(final ImGuiViewport vp, final String str) {
//            final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//            glfwSetWindowTitle(data.window, str);
//        }
//    }
//
//    private final class SetWindowFocusFunction extends ImPlatformFuncViewport {
//        @Override
//        public void accept(final ImGuiViewport vp) {
//            if (glfwHasFocusWindow) {
//                final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//                glfwFocusWindow(data.window);
//            }
//        }
//    }
//
//    private static final class GetWindowFocusFunction extends ImPlatformFuncViewportSuppBoolean {
//        @Override
//        public boolean get(final ImGuiViewport vp) {
//            final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//            return glfwGetWindowAttrib(data.window, GLFW_FOCUSED) != 0;
//        }
//    }
//
//    private static final class GetWindowMinimizedFunction extends ImPlatformFuncViewportSuppBoolean {
//        @Override
//        public boolean get(final ImGuiViewport vp) {
//            final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//            return glfwGetWindowAttrib(data.window, GLFW_ICONIFIED) != 0;
//        }
//    }
//
//    private final class SetWindowAlphaFunction extends ImPlatformFuncViewportFloat {
//        @Override
//        public void accept(final ImGuiViewport vp, final float f) {
//            if (glfwHasWindowAlpha) {
//                final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//                glfwSetWindowOpacity(data.window, f);
//            }
//        }
//    }
//
//    private static final class RenderWindowFunction extends ImPlatformFuncViewport {
//        @Override
//        public void accept(final ImGuiViewport vp) {
//            final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//            glfwMakeContextCurrent(data.window);
//        }
//    }
//
//    private static final class SwapBuffersFunction extends ImPlatformFuncViewport {
//        @Override
//        public void accept(final ImGuiViewport vp) {
//            final ImGuiViewportDataGlfw data = (ImGuiViewportDataGlfw) vp.getPlatformUserData();
//            glfwMakeContextCurrent(data.window);
//            glfwSwapBuffers(data.window);
//        }
//    }
//
//    private void initPlatformInterface() {
//
//        final ImGuiPlatformIO platformIO = ImGui.getPlatformIO();
//
//        // Register platform interface (will be coupled with a renderer interface)
//        platformIO.setPlatformCreateWindow(new CreateWindowFunction());
//        platformIO.setPlatformDestroyWindow(new DestroyWindowFunction());
//        platformIO.setPlatformShowWindow(new ShowWindowFunction());
//        platformIO.setPlatformGetWindowPos(new GetWindowPosFunction());
//        platformIO.setPlatformSetWindowPos(new SetWindowPosFunction());
//        platformIO.setPlatformGetWindowSize(new GetWindowSizeFunction());
//        platformIO.setPlatformSetWindowSize(new SetWindowSizeFunction());
//        platformIO.setPlatformSetWindowTitle(new SetWindowTitleFunction());
//        platformIO.setPlatformSetWindowFocus(new SetWindowFocusFunction());
//        platformIO.setPlatformGetWindowFocus(new GetWindowFocusFunction());
//        platformIO.setPlatformGetWindowMinimized(new GetWindowMinimizedFunction());
//        platformIO.setPlatformSetWindowAlpha(new SetWindowAlphaFunction());
//        platformIO.setPlatformRenderWindow(new RenderWindowFunction());
//        platformIO.setPlatformSwapBuffers(new SwapBuffersFunction());
//
//        // Register main window handle (which is owned by the main application, not by us)
//        // This is mostly for simplicity and consistency, so that our code (e.g. mouse handling etc.) can use same logic for main and secondary viewports.
//        final ImGuiViewport mainViewport = ImGui.getMainViewport();
//        final ImGuiViewportDataGlfw data = new ImGuiViewportDataGlfw();
//        data.window = windowPtr;
//        data.windowOwned = false;
//        mainViewport.setPlatformUserData(data);
//    }
//
//    private void shutdownPlatformInterface() {
//    }
//
//    private static final class ImGuiViewportDataGlfw {
//        long window;
//        boolean windowOwned = false;
//        int ignoreWindowPosEventFrame = -1;
//        int ignoreWindowSizeEventFrame = -1;
//    }*/
}
