package dk.sebsa.coal.graph;

import dk.sebsa.coal.trash.Trash;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL42.*;

/**
 * @author sebs
 */
public class VAO extends Trash {
    private final int vertexSize;
    private int amount;
    private final int vboVertexID;
    private final int vaoID;

    public VAO(float[] vertices, int vertexSize) {
        this.vertexSize = vertexSize;

        // Vertex
        FloatBuffer vertex_data = MemoryUtil.memAllocFloat(vertices.length);
        vertex_data.put(vertices);
        vertex_data.flip();

        // VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboVertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertex_data, GL_STATIC_DRAW);
        glVertexAttribPointer(0, vertexSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
        MemoryUtil.memFree(vertex_data);
        amount = vertices.length/vertexSize;
    }


    public void put(float[] vertices) {
        FloatBuffer vertex_data = MemoryUtil.memAllocFloat(vertices.length);
        vertex_data.put(vertices);
        vertex_data.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertex_data, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        amount = vertices.length/vertexSize;
    }

    public void draw(int mode) {
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);

        glDrawArrays(mode, 0, amount);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        glDeleteBuffers(vboVertexID);
        glDeleteVertexArrays(vaoID);
    }
}
