package dk.sebsa.coal.graph;

import dk.sebsa.Coal;
import dk.sebsa.coal.Application;
import dk.sebsa.coal.asset.AssetManager;
import dk.sebsa.coal.enums.PolygonMode;
import dk.sebsa.coal.graph.renderes.Render2D;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

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
        FBO.renderFBO(app, prevFBO, RenderStage.r);
        Render2D.prepare();
        Render2D.drawTextureWithTextCoords((Texture) AssetManager.getAsset("internal/textures/Chicken.png"), app.window.rect, new Rect(0,0,1,1));
        Render2D.unprepare();
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
