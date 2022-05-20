package dk.sebsa.coal.graph.renderes;

import dk.sebsa.coal.ecs.collision.Collider2D;

/**
 * @author sebs
 */
public record Collision(Collider2D main, Collider2D collider) {
}
