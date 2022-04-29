package dk.sebsa.coal.events;

import dk.sebsa.coal.enums.EventTypes;

/**
 * @author sebs
 * @since 1.0.0
 */
public abstract class Event {
    public abstract EventTypes eventType();
}
