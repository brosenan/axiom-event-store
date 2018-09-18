package axiom.event_store;

/**
 * A continuation can be returned as the last element in an event collection, representing the remaining results.
 * It provides an opaque representation of the continuation, allowing the remaining elements to be retrieved.
 */
public interface Continuation {
	/**
	 * @return A binary representation of the continuation.
	 */
	byte[] serialize();
}
