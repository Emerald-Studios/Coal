package dk.sebsa.sandbox;

import dk.sebsa.coal.ecs.Component;
import dk.sebsa.coal.io.GLFWInput;
import dk.sebsa.coal.math.Time;
import org.lwjgl.glfw.GLFW;

/**
 * @author sebs
 */
public class Test2DMovement extends Component {
    private static final int speed = 300;
    @Override
    protected void load() {

    }

    @Override
    protected void update(GLFWInput input) {
        float move = speed * Time.getDeltaTime();
        if(input.isKeyDown(GLFW.GLFW_KEY_S)) move2D(0, -move);
        if(input.isKeyDown(GLFW.GLFW_KEY_W)) move2D(0, move);
        if(input.isKeyDown(GLFW.GLFW_KEY_A)) move2D(-move, 0);
        if(input.isKeyDown(GLFW.GLFW_KEY_D)) move2D(move, 0);
    }

    @Override
    protected void lateUpdate(GLFWInput input) {

    }
}
