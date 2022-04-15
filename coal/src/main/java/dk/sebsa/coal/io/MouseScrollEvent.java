/**
 * 
 */
package dk.sebsa.coal.io;

import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;

/**
 * @author Sebsa
 * @since 1.0.0-SNAPSHOT
 */
public class MouseScrollEvent extends Event {
	public EventTypes eventType() { return EventTypes.MouseScroll; }
	public double offsetX, offsetY;
}
