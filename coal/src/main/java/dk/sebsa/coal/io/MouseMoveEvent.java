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
public class MouseMoveEvent extends Event {
	public EventTypes eventType() { return EventTypes.MouseMove; }
	
	public int[] mousePosX = new int[1];
	public int[] mousePosY = new int[1];
	public int[] offsetMousePosX = new int[1];
	public int[] offsetMousePosY = new int[1];
}
