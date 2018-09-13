package axiom.event_store;

/**
 * Represents an event
 */
public interface Event {
	/**
	 * @return The event's unique ID
	 */
	String getId();
	/**
	 * @return The event type
	 */
	String getType();
	/**
	 * @return The event's non-unique key
	 */
	byte[] getKey();
	/**
	 * @return The event's quantitative change.
	 */
	int getChange();
	/**
	 * @return The data associated with the key
	 */
	byte[] getData();
	/**
	 * @return If not null, the event represents a pair of events.
	 */
	byte[] getRemoved();
	/**
	 * @return an implementation-sepcific representation of this event.
	 */
	Object getUnderlyingRepresentation();
}
