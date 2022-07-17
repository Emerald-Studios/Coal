package dk.sebsa.coal.physm;

import dk.sebsa.Coal;
import dk.sebsa.coal.graph.Rect;
import dk.sebsa.coal.math.Vector2f;
import dk.sebsa.coal.physm.M2D.MAABBCollider2D;
import dk.sebsa.coal.physm.M2D.MCollider2D;
import dk.sebsa.coal.tasks.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebs
 */
public class PhysM2DTask extends Task {
    @Override
    protected String name() { return "PhysM2DTask"; }

    @Override
    public void run() {
        overlapsThisFrame.clear();
        for(int m = 0; m < MCollision2D.movers.size(); m++) {
            MCollider2D mover = MCollision2D.movers.get(m);
            for (int s = 0; s < MCollision2D.solids.size(); s++) {
                MCollider2D solid = MCollision2D.solids.get(s);
                if(mover.equals(solid)) continue;


                if(mover instanceof MAABBCollider2D && solid instanceof MAABBCollider2D) compareCollision((MAABBCollider2D) mover, (MAABBCollider2D) solid);
            }

            MCollision2D.solids.add(mover);
            mover.getEntity().transform.clean();
        }
        MCollision2D.solids.addAll(MCollision2D.movers);

        // Cleanup - THIS HAPPENS AT THE START OF THE NEXT FRAME (FOR RENDERING PURORSES)
        // MCollision2D.solids.clear();
        // MCollision2D.movers.clear();
    }

    public static final List<Rect> overlapsThisFrame = new ArrayList<>();
    public static final Rect overlap = new Rect(), solidRect = new Rect(), moverRect = new Rect();
    private static void compareCollision(MAABBCollider2D mover, MAABBCollider2D solid) {
        moverRect.set(mover.getWorldPositionRect());
        solidRect.set(solid.getWorldPositionRect());

        if (solidRect.getOverlap(moverRect, overlap)) {
            if(Coal.DEBUG) overlapsThisFrame.add(new Rect().set(overlap));

            if (mover.isTrigger || solid.isTrigger) {
                mover.getEntity().callTriggerCallback();
                solid.getEntity().callTriggerCallback();
            } else {
                mover.getEntity().callCollisionCallback();
                solid.getEntity().callCollisionCallback();

                if (overlap.width < overlap.height) {
                    if (moverRect.x < overlap.x) mover.getEntity().transform.move(new Vector2f(-overlap.width, 0));
                    else mover.getEntity().transform.move(new Vector2f(overlap.width, 0));
                } else {
                    if (moverRect.y > overlap.y) mover.getEntity().transform.move(new Vector2f(0, overlap.height));
                    else mover.getEntity().transform.move(new Vector2f(0, -overlap.height));
                }
            }
        }
    }
}
