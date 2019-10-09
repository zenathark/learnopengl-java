package oglib.components;

import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    final public String sourcePath;
    final protected int shaderId;

    public Shader(String sourcePath, ShaderType type) throws IOException, CreateException, CompileException {
        var glType = 0;
        switch (type) {
        case VERTEX:
            glType = GL_VERTEX_SHADER;
            break;
        case FRAGMENT:
            glType = GL_FRAGMENT_SHADER;
            break;
        }
        this.sourcePath = sourcePath;
        final var source = Resources.toString(Resources.getResource(sourcePath), Charsets.UTF_8);
        shaderId = glCreateShader(glType);
        if (shaderId == 0) {
            throw new CreateException("Error loading shader");
        }
        glShaderSource(shaderId, source);
        glCompileShader(shaderId);
        var err = glGetShaderInfoLog(shaderId);
        if (err != null && !err.isEmpty())
            throw new CompileException("Error compiling shader " + err);
    }

    public int getId() {
        return shaderId;
    }
}