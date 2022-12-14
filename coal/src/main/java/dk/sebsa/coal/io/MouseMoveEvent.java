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
public class MouseMoveEvent extends Event {
	public EventTypes eventType() { return EventTypes.MouseMove; }
	
	public final int[] mousePosX = new int[1];
	public final int[] mousePosY = new int[1];
	public final int[] offsetMousePosX = new int[1];
	public final int[] offsetMousePosY = new int[1];
}
