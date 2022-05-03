package dk.sebsa.coal.graph;

import dk.sebsa.coal.asset.Asset;
import dk.sebsa.coal.asset.AssetLocation;
import dk.sebsa.coal.math.Color;
import dk.sebsa.coal.math.Matrix4x4f;
import dk.sebsa.coal.util.FileUtils;
import lombok.SneakyThrows;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author sebs
 * @since 1.0.0
 */
public class GLSLShaderProgram extends Asset {
    private int programId;
    private int vertexShaderId, fragmentShaderId;
    private final Map<String, Integer> uniforms = new HashMap<>();

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
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            log("Program failed to link.\n" + glGetProgramInfoLog(programId, glGetProgrami(programId, GL_INFO_LOG_LENGTH)));
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

    public void createUniform(String uniformName) throws Exception {
        log(name + " - Creating uniform named - " + uniformName);
        if(uniforms.containsKey(uniformName)) return;
        int uniformLocation = glGetUniformLocation(programId,
                uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" +
                    uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
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

    public void setUniform(String name, Matrix4x4f value) {
        int location = uniforms.get(name);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.getBuffer(buffer);

        if(location != -1) glUniformMatrix4fv(location, false, buffer);
        buffer.flip();
    }

    public void setUniform(String uniformName, float x, float y, float z, float w) {
        glUniform4f(uniforms.get(uniformName), x, y, z, w);
    }

    public void setUniform(String uniformName, float x, float y) {
        glUniform2f(uniforms.get(uniformName), x, y);
    }

    public void setUniformAlt(String uniformName, Color value) {
        glUniform4f(uniforms.get(uniformName), value.r, value.g, value.b, value.a);
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }
}
