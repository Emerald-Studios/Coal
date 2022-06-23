package dk.sebsa.coal.graph.renderes;

import dk.sebsa.coal.physics.collision.BoxCollider2D;
import dk.sebsa.coal.physics.collision.Collider2D;
import dk.sebsa.coal.tasks.Task;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author sebs
 */
public class ColliderCalculationTask2D extends Task {
    @Override
    protected String name() { return this.getClass().getSimpleName(); }

    private final static List<Collider2D> collider2D = new ArrayList<>();
    @Getter private final static List<Collision> collision = Collections.synchronizedList(new ArrayList<>());
    public static volatile CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void run() {
        latch = new CountDownLatch(1);

        collider2D.clear();
        collision.clear();
        for(Collider2D collider : Collider2D.getCOLLIDERS()) if(collider.isEnabled()) collider2D.add(collider);

        while(!collider2D.isEmpty()) {
            Collider2D base = collider2D.get(0);

            for(int i = 1; i < collider2D.size(); i++) {
                Collider2D collider = collider2D.get(i);

                // Box Collider
                if(collider instanceof BoxCollider2D)
                    if(base.collides((BoxCollider2D) collider)) {
                        if(!base.isTrigger) collision.add(new Collision(base, collider));
                        if(!collider.isTrigger) collision.add(new Collision(collider, base));
                    }
            }
            collider2D.remove(0);
        }

        latch.countDown();
    }
}
