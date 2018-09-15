package axiom.event_store;

/**
 * An event domain represents a kind of event.
 * A user of an EventStoreService provides an EventDomain implementation in order to allow it to use their own events.
 * An EventDomain implements methods for extracting information out of events 
 * (represented as opaque Objects), and to serialize/deserialize them to binary representations.
 */
public interface EventDomain {
	/**
	 * @param event An opaque event object
	 * @return A unique ID of that event.
	 */
	String id(Object event);
	
	/**
	 * @param event An opaque event object
	 * @return The event type
	 */
	String type(Object event);
	
	/**
	 * @param event An opaque event object
	 * @return A non-unique event key
	 */
	byte[] key(Object event);
	
	/**
	 * @param event An opaque event object
	 * @return The quantitative change represented by the event. 1 means addition, -1 means deletion.
	 */
	int change(Object event);
	
	/**
	 * @param event An opaque event object
	 * @return A binary representation of the "body" of an event. Should not include the key, change and id.
	 */
	byte[] body(Object event);
	
	/**
	 * Serialize the event into binary format.
	 * @param event An opaque event object
	 * @return The event, represented as binary.
	 */
	byte[] serialize(Object event);
	
	/**
	 * Deserialize an event.
	 * @param ser A serialization of an event, as received from serialize().
	 * @return An event object, equivalent to the one provided to serialize() to receive ser.
	 */
	Object deserialize(byte[] ser);
}
