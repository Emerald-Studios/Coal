package dk.sebsa.coal.events;

import dk.sebsa.coal.enums.EventTypes;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public abstract class Event {
    public abstract EventTypes eventType();
}
