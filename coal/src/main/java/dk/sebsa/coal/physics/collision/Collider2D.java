package dk.sebsa.coal.physics.collision;

import dk.sebsa.coal.ecs.Component;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebs
 */
public abstract class Collider2D extends Component {
    @Getter
    private static final List<Collider2D> COLLIDERS = new ArrayList<>();
    public abstract boolean collides(BoxCollider2D collider2D);

    public Collider2D() {
        COLLIDERS.add(this);
    }

    @Override
    public void destroy() {
        COLLIDERS.remove(this);
    }
}
