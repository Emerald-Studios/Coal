package dk.sebsa.arcade;

import dk.sebsa.coal.Application;

/**
 * @author sebs
 */
public abstract class Game {
    public abstract void renderUI();
    public abstract void cleanup();
    public abstract void frame();
    public abstract void init(Application app);
    public abstract boolean alive();
}
