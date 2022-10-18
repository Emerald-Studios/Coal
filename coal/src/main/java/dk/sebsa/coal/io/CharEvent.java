package dk.sebsa.coal.io;
import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;

/**
* @author sebs
* @since 1.3.0
*/
public class CharEvent extends Event {
    public EventTypes eventType() { return EventTypes.Char; }
    public int codePoint;
}