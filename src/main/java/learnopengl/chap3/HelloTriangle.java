package learnopengl.chap3;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.IOException;
import java.nio.*;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class HelloTriangle {
    public static long init() {
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

        var window = glfwCreateWindow(300, 300, "Hello, World!", NULL, NULL);
        glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(w, true);
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
        return window;
    }

    public static void createTriangle(long window) throws IOException, Exception {
        GL.createCapabilities();

        final var vertexSource = Resources.toString(Resources.getResource("hello_triangle.vert"), Charsets.UTF_8);
        final var vertexShader = glCreateShader(GL_VERTEX_SHADER);
        if (vertexShader == 0) {
            throw new Exception("Error loading vertex shader");
        }
        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);

        var err = glGetShaderInfoLog(vertexShader);
        if (err != null && !err.isEmpty())
            throw new Exception("Error compiling fragment shader " + err);

        final var fragmentSource = Resources.toString(Resources.getResource("hello_triangle.frag"), Charsets.UTF_8);
        final var fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        if (fragmentShader == 0) {
            throw new Exception("Error loading fragment shader");
        }
        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);

        err = glGetShaderInfoLog(fragmentShader);
        if (err != null && !err.isEmpty())
            throw new Exception("Error compiling vertex shader " + err);

        final var shaderProgram = glCreateProgram();
        if (shaderProgram == 0)
            throw new Exception("Error creating program");
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        err = glGetProgramInfoLog(shaderProgram);
        if (err != null && !err.isEmpty())
            throw new Exception("Error compiling program " + err);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        final var vertices = new float[] { -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.0f, 0.5f, 0.0f };

        final var VAO = glGenVertexArrays();
        final var VBO = glGenBuffers();
        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        System.out.println(VAO);
        System.out.println(VBO);
        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glUseProgram(shaderProgram);
            glBindVertexArray(VAO);
            glDrawArrays(GL_TRIANGLES, 0, 3);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void run() throws IOException, Exception {
        System.out.printf("Hello LWJGL %s", Version.getVersion());
        var w = init();
        createTriangle(w);
        glfwFreeCallbacks(w);
        glfwDestroyWindow(w);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}