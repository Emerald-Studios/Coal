package dk.sebsa.coal.graph;

import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.util.FileUtils;
import lombok.SneakyThrows;

import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.*;

public class GLSLShaderProgram extends Asset {
    private int programId;
    private int vertexShaderId, fragmentShaderId;

    public GLSLShaderProgram(AssetLocation location) {
        super(location);
    }

    @Override @SneakyThrows
    public void load() {
        log("Creating shader program...");
        programId = glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Could not create Shader: " + glGetError());
        }

        // Create shader
        log("Loading shader code...");
        String[] shaderCode = FileUtils.readAllLines(location.asStream()).split("// ### END VERTEX-SHADER### //");
        vertexShaderId = createShader(shaderCode[0], GL_VERTEX_SHADER);
        fragmentShaderId = createShader(shaderCode[1], GL_FRAGMENT_SHADER);
        log("Link...");
        link();
        log("Done!");
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    // Shade use code
    public void bind() { glUseProgram(programId); }
    public void unbind() { glUseProgram(0); }

    @Override
    public void destroy() {
        unbind();
        if (programId != 0) {
            log("Shader bye bye: " + name);
            glDeleteProgram(programId);
        }
    }
}
