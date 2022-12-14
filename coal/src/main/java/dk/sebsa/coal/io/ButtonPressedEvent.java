/**
 * 
 */
package dk.sebsa.coal.io;

import dk.sebsa.coal.enums.EventTypes;
import dk.sebsa.coal.events.Event;
import dk.sebsa.coal.math.Vector2f;

/**
 * @author sebs
 * @since 1.0.0
 */
public class ButtonPressedEvent extends Event {
	public EventTypes eventType() { return EventTypes.ButtonPressed; }
	public int button;
	public Vector2f mouse;
}
