package dk.sebsa.coal.graph;

public abstract class RenderStage {
    public abstract String getName();
    private boolean init = false; // Init is done later, so it has opengl context

    protected void render() {

    }
}
