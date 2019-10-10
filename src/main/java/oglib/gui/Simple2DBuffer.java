package oglib.gui;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import oglib.components.Color;

import static com.google.common.base.Preconditions.*;

public class Simple2DBuffer {

    private float[] vertices = new float[] {
            // positions // colors // texture coords
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, // top right
            1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, // bottom right
            -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, // bottom left
            -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f // top left
    };
    private byte[] indices = new byte[] { 0, 1, 3, 1, 2, 3 };

    final private int vao;
    final private int vbo;
    final private int ebo;
    final private int textureId;
    final private int width;
    final private int height;
    final private ByteBuffer screenBuffer;
    final private byte[] screen;
    final private byte[] blankScreen;
    private boolean updated = false;
    final private static int posLocation = 0;
    final private static int colorLocation = 1;
    final private static int uvLocation = 2;

    public Simple2DBuffer(int width, int height) {
        checkArgument(width > 0, "Width must be positive");
        checkArgument(height > 0, "Height must be positive");

        this.width = width;
        this.height = height;
        screen = new byte[this.width * this.height * 3];
        blankScreen = new byte[this.width * this.height * 3];

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        var vboBuffer = ByteBuffer.allocateDirect(vertices.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vboBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vboBuffer, GL_STATIC_DRAW);
        // Position Attribute
        // size = elements on a row (8) * float size (4)
        glVertexAttribPointer(posLocation, 3, GL_FLOAT, false, 8 * 4, 0);
        glEnableVertexAttribArray(posLocation);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        var eboBuffer = ByteBuffer.allocateDirect(indices.length).order(ByteOrder.nativeOrder());
        eboBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, eboBuffer, GL_STATIC_DRAW);

        // Color Attribute
        // size = elements on a row (8) * float size (4)
        // offset = position vertex elements (3) * float size (4)
        glVertexAttribPointer(colorLocation, 3, GL_FLOAT, false, 8 * 4, 3 * 4);
        glEnableVertexAttribArray(colorLocation);

        // UV Attribute
        // size = elements on a row (8) * float size (4)
        // offset = (position vertex elements (3) + color vertex elements (3)) * float
        // size (4)
        glVertexAttribPointer(uvLocation, 2, GL_FLOAT, false, 8 * 4, (3 + 3) * 4);
        glEnableVertexAttribArray(uvLocation);

        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        screenBuffer = ByteBuffer.allocateDirect(this.width * this.height * 3).order(ByteOrder.nativeOrder());

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean value) {
        this.updated = value;
    }

    /**
     * Sets the pixel at x,y to the given color.
     */
    public void set(int x, int y, Color color) {
        final var idx = (y * width + x) * 3;
        screen[idx] = color.red;
        screen[idx + 1] = color.green;
        screen[idx + 2] = color.blue;
        updated = true;
    }

    public void set(int x, int y, int red, int green, int blue) {
        checkArgument(red >= 0 && red <= 255, "Red channel must have values between 0 and 255");
        checkArgument(green >= 0 && green <= 255, "Green channel must have values between 0 and 255");
        checkArgument(blue >= 0 && blue <= 255, "Blue channel must have values between 0 and 255");

        final var idx = (y * width + x) * 3;
        screen[idx] = (byte) (0xFF & red);
        screen[idx + 1] = (byte) (0xFF & green);
        screen[idx + 2] = (byte) (0xFF & blue);
        updated = true;
    }

    public void set(int idx, int red, int green, int blue) {
        checkArgument(red >= 0 && red <= 255, "Red channel must have values between 0 and 255");
        checkArgument(green >= 0 && green <= 255, "Green channel must have values between 0 and 255");
        checkArgument(blue >= 0 && blue <= 255, "Blue channel must have values between 0 and 255");

        var realIdx = idx * 3;

        screen[realIdx] = (byte) (0xFF & red);
        screen[realIdx + 1] = (byte) (0xFF & green);
        screen[realIdx + 2] = (byte) (0xFF & blue);
        updated = true;
    }

    public void clear() {
        System.arraycopy(blankScreen, 0, screen, 0, width * height);
        updated = true;
    }

    public void draw() {
        glBindVertexArray(vao);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBindTexture(GL_TEXTURE_2D, textureId);
        if (updated) {
            screenBuffer.clear();
            screenBuffer.put(screen).flip();
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB,  GL_UNSIGNED_BYTE, screenBuffer);
            updated = false;
        }
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);
    }
}