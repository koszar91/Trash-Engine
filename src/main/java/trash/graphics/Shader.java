package trash.graphics;

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
     * @param vertexSourceFilename .
     * @param fragmentSourceFilename .
     * @throws IOException When at least one of the given filenames does not exist.
     * @throws IllegalStateException When compilation of at least one shader failed.
     */
    public Shader(String vertexSourceFilename, String fragmentSourceFilename) throws IOException, IllegalStateException {

        this.vertexSourceFilename = vertexSourceFilename;
        this.fragmentSourceFilename = fragmentSourceFilename;

        var vertexSource = String.join("\n", Files.readAllLines(Paths.get(PATH + "/" + vertexSourceFilename)));
        var fragmentSource = String.join("\n", Files.readAllLines(Paths.get(PATH + "/" + fragmentSourceFilename)));

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
        return resultID;
    }
}
