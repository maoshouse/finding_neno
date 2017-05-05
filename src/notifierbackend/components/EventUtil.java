package notifierbackend.components;

import Prism.core.Event;
import Prism.core.PrismConstants;

public class EventUtil {
    public static Event makeNotification(String eventName) {
	Event event = new Event(eventName);
	event.eventType = PrismConstants.REPLY;
	return event;
    }

    public static Event makeRequest(String eventName) {
	Event event = new Event(eventName);
	event.eventType = PrismConstants.REQUEST;
	return event;
    }
}
