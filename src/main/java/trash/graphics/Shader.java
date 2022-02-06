package trash.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import static org.lwjgl.opengl.GL20.*;


public class Shader {

    private static final String PATH = "src/main/resources/shaders";

    private final int vertexID, fragmentID, programID;
    private final String vertexSourceFilename;
    private final String fragmentSourceFilename;

    /**
     * Constructor initializes and allocates shader program on the GPU
     * @param vertexSrcFilename .
     * @param fragmentSrcFilename .
     * @throws IOException When one of the given filenames does not exist.
     * @throws IllegalStateException When compilation of a shader has failed.
     */
    public Shader(String vertexSrcFilename, String fragmentSrcFilename)
            throws IOException, IllegalStateException {


        this.vertexSourceFilename = vertexSrcFilename;
        this.fragmentSourceFilename = fragmentSrcFilename;

        var vertexSource = String.join("\n", Files.readAllLines(Paths.get(PATH + "/" + vertexSrcFilename)));
        var fragmentSource = String.join("\n", Files.readAllLines(Paths.get(PATH + "/" + fragmentSrcFilename)));

        vertexID = createAndCompileShader(vertexSource, GL_VERTEX_SHADER);
        fragmentID = createAndCompileShader(fragmentSource, GL_FRAGMENT_SHADER);

        programID = createAndLinkProgram();
    }

    public void bind() {
        glUseProgram(programID);
    }

    public static void unbind() {
        glUseProgram(0);
    }

    public void setBoolean(String name, boolean value) {
        glUniform1f(getLocation(name), value ? 1 : 0);
    }

    public void setInteger(String name, int value) {
        glUniform1i(getLocation(name), value);
    }

    public void setFloat(String name, float value) {
        glUniform1f(getLocation(name), value);
    }

    public void setVector3(String name, Vector3f vector) {
        var buffer = BufferUtils.createFloatBuffer(3);
        vector.get(buffer);
        glUniform3fv(getLocation(name), buffer);
    }

    public void setMatrix4(String name, Matrix4f matrix) {
        var buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        buffer.flip();
        glUniformMatrix4fv(getLocation(name), false, buffer);
    }

    public String getVertexSourceFilename() {
        return vertexSourceFilename;
    }

    public String getFragmentSourceFilename() {
        return fragmentSourceFilename;
    }

    private int createAndCompileShader(String src, int type) throws IllegalStateException {
        int resultID = glCreateShader(type);
        glShaderSource(resultID, src);
        glCompileShader(resultID);
        if (glGetShaderi(resultID, GL_COMPILE_STATUS) == GL_FALSE) {
            var infoLogLength = glGetShaderi(resultID, GL_INFO_LOG_LENGTH);
            var infoLog = glGetShaderInfoLog(resultID, infoLogLength);
            throw new IllegalStateException("Shader compilation failed. Info log: '" + infoLog + "'.");
        }
        return resultID;
    }

    private int createAndLinkProgram() {
        int resultID = glCreateProgram();
        glAttachShader(resultID, fragmentID);
        glAttachShader(resultID, vertexID);
        glLinkProgram(resultID);
        if (glGetProgrami(resultID, GL_LINK_STATUS) == GL_FALSE) {
            var infoLogLength = glGetProgrami(resultID, GL_INFO_LOG_LENGTH);
            var infoLog = glGetProgramInfoLog(resultID, infoLogLength);
            throw new IllegalStateException("Program linking failed. Info log: '" + infoLog + "'.");
        }
        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
        return resultID;
    }

    private int getLocation(String name) {
        return glGetUniformLocation(programID, name);
    }
}
