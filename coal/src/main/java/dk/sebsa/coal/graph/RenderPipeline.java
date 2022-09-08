package dk.sebsa.coal.graph;

import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.enums.PolygonMode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author sebs
 * @since 1.0.0
 */
public class RenderPipeline {
    private final List<RenderStage> stages;
    private boolean hasPrintedDebugMessageYet;
    public PolygonMode polygonMode = PolygonMode.Fill;

    private RenderPipeline(List<RenderStage> stages) {
        this.stages = stages;
        hasPrintedDebugMessageYet = !Coal.DEBUG;
    }

    public void renderStageAll(Application app) {
        if(!hasPrintedDebugMessageYet) {
            hasPrintedDebugMessageYet = true;
            Coal.logger.log("Rendering Pipeline: ", getClass().getSimpleName());
            for (RenderStage stage : stages) {
                Coal.logger.log(" - " + stage.getName(), getClass().getSimpleName());
            }
        }

        if (polygonMode.equals(PolygonMode.Line)) glPolygonMode( GL_FRONT_AND_BACK, GL_LINE  );
        else glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );

        FBO prevFBO = null;
        for(RenderStage stage : stages) {
            try {
                prevFBO = stage.render(prevFBO);
            } catch (Exception | Error e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);

                Coal.logger.error("Render stage: " + stage.getName() + ", failed to run. Error: " + e.getMessage() + "\nStacktrace: " + sw, getClass().getSimpleName());
                Coal.shutdownDueToError();
            }
        }

        // Render the FBO
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
        FBO.renderFBO(app, prevFBO, app.window.renderRect, new Rect(0, 0, 1, 1));
    }

    public static class RenderPipelineBuilder {
        private List<RenderStage> stages = new ArrayList<>();

        public RenderPipelineBuilder appendStage(RenderStage stage) { stages.add(stage); return this; }

        public RenderPipeline build() {
            RenderPipeline pipeline = new RenderPipeline(stages);
            stages = new ArrayList<>();

            return pipeline;
        }
    }
}
