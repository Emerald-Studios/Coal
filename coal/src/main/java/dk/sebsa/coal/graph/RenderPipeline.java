package dk.sebsa.coal.graph;

import dk.sebsa.Coal;
import dk.sebsa.coal.enums.PolygonMode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class RenderPipeline {
    private final List<RenderStage> stages;
    private boolean hasPrintedDebugMessageYet = false;
    public PolygonMode polygonMode = PolygonMode.Fill;

    private RenderPipeline(List<RenderStage> stages) {
        this.stages = stages;
        hasPrintedDebugMessageYet = !Coal.DEBUG;
    }

    public void renderStageAll() {
        if(!hasPrintedDebugMessageYet) {
            hasPrintedDebugMessageYet = true;
            Coal.logger.log("Rendering Pipeline: ", getClass().getSimpleName());
            for (RenderStage stage : stages) {
                Coal.logger.log(" - " + stage.getName(), getClass().getSimpleName());
            }
        }

        if (polygonMode.equals(PolygonMode.Line)) glPolygonMode( GL_FRONT_AND_BACK, GL_LINE  );
        else glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );

        for(RenderStage stage : stages) {
            try {
                stage.render();
            } catch (Exception | Error e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);

                Coal.logger.error("Render stage: " + stage.getName() + ", failed to run. Error: " + e.getMessage() + "\nStacktrace: " + sw.toString(), getClass().getSimpleName());
            }
        }
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
