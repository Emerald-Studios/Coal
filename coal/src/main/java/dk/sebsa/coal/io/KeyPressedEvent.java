/**
 * 
 */
package dk.sebsa.coal.io;

import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;

/**
 * @author sebs
 * @since 1.0.0
 */
public class KeyPressedEvent extends Event {
	public EventTypes eventType() { return EventTypes.KeyPressed; }
	public int key;
}
