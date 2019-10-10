package oglib;

import java.io.IOException;

import oglib.components.CompileException;
import oglib.components.CreateException;
import oglib.components.Program;
import oglib.game.GameState;
import oglib.gui.Simple2DBuffer;
import oglib.gui.WindowGL;

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

public class App {
    public static void main(String[] args) {
        var gameState = GameState.getGameState();
        var width = 300;
        var height = 300;
        var w = new WindowGL(width, height, "Drawing Program", gameState);

        try {
            var program = new Program("screen.vert", "screen.frag");
            var screen = new Simple2DBuffer(width, height);
            var x = 255;
            for (int i = 0; i < width; i++) {
                screen.set(i, 200, x, x, x);
            }
            while (!w.windowShouldClose()) {
                glClearColor(0f, 0f, 0f, 1.0f);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                program.use();
                screen.draw();
                w.swapBuffers();
                w.pollEvents();
            }
            w.destroy();
        } catch (IOException | CreateException | CompileException e) {
            e.printStackTrace();
        }

    }
}