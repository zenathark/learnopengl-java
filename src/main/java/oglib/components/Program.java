package oglib.components;

import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;

public class Program {
    final protected int programId;
    final protected Shader vertexShader;
    final protected Shader fragmentShader;

    public Program(String vertexShaderPath, String fragmentShaderPath)
            throws IOException, CreateException, CompileException {
        vertexShader = new Shader(vertexShaderPath, ShaderType.VERTEX);
        fragmentShader = new Shader(fragmentShaderPath, ShaderType.FRAGMENT);
        programId = glCreateProgram();
        if (programId == 0)
            throw new CreateException("Error creating program");
        glAttachShader(programId, vertexShader.shaderId);
        glAttachShader(programId, fragmentShader.shaderId);
        glLinkProgram(programId);

        var err = glGetProgramInfoLog(programId);
        if (err != null && !err.isEmpty())
            throw new CompileException("Error compiling program " + err);
    }

    public int getId() {
        return programId;
    }

    public void use() {
        glUseProgram(programId);
    }

}