import dk.sebsa.coal.ecs.Component;

public class Test extends Component {
    protected void load() {
        Coal.logger.log("Hello World!");
    }
    protected void update(GLFWInput input) {

    }
    protected void lateUpdate(GLFWInput input) {

    }
}