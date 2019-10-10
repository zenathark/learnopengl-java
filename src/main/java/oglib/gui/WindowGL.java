package oglib.gui;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.Callbacks.*;

import oglib.game.*;

/**
 * This class holds the reference to a GLFW window.
 * 
 * It's methods allow to manipulate all window's properties.
 */
public class WindowGL {
    public final long window;
    private boolean windowMinimized = false;
    private final IGameState gameState;

    public WindowGL(int width, int height, String title, IGameState gameState) {
        this.gameState = gameState;
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        // FIXME Unable to customize keys' callback
        glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(w, true);
            if (key == GLFW_KEY_A && action == GLFW_RELEASE)
                gameState.setKey(Key.getKeys().get(key), IGameState.KeyStates.RELEASED);
        });

        try {
            var stack = stackPush();
            var pWidth = stack.mallocInt(1);
            var pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            var vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);

        } finally {
            stackPop();
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities(); // Makes main window the current context
    }

    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Check if window is marked for closing
     */
    public boolean windowShouldClose() {
        return glfwWindowShouldClose(window);
    }

    public boolean isWindowMinimized() {
        return windowMinimized;
    }

    public boolean isWindowHidden() {
        return (glfwGetWindowAttrib(window, GLFW_VISIBLE) == GL_FALSE);
    }

    public void hideWindow() {
        glfwHideWindow(window);
    }

    public void unhideWindow() {
        glfwShowWindow(window);
    }

    public void closeWindow() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

}