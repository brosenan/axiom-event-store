package axiom.event_store;

import java.io.IOException;

/**
 * Stores events.
 */
public interface EventStore {
	/**
	 * Create an event object. The underlying representation can be implementation-specific.
	 * 
	 * @param id A unique event ID.
	 * @param type The type of event.
	 * @param key A non-unique key.
	 * @param change 1 for a creation event, -1 for a deletion event.
	 * @param data Opaque data associated with the key
	 * @param removed Optional. If exists, this event represents a pair of events, where the second event has data = removed and change = -change.
	 * @return An event object.
	 */
	Event event(String id, String type, byte[] key, int change, byte[] data, byte[] removed);
	
	/**
	 * @return The number of shards in the event-store.
	 */
	int numShards();
	
	/**
	 * @return The number of replicas each shard has.
	 */
	int replicationFactor();
	
	/**
	 * Create an association between two types. This should be called for each replica of each shard.
	 * 
	 * @param type1 The type to be associated with type2
	 * @param type2 The type to be associated with type1
	 * @param shard The shard in which to create this association.
	 * @param replica The replica in which to create this association.
	 * @throws IOException If the association cannot be make.
	 */
	void associate(String type1, String type2, int shard, int replica) throws IOException;
	
	/**
	 * Returns the types associated with the given type.
	 * 
	 * @param type The type for which we fetch the associations.
	 * @return All the types that were associated with type. Regardless of whether they were given in the first or second position. The returned Iterable only provides a regular Iterator.
	 * @throws IOException If associations cannot be fetched.
	 */
	Iterable<String> getAssociation(String type) throws IOException;
	
	/**
	 * Stores an event in one replica. The shard is determined according to the key.
	 * 
	 * @param event The event to be stored.
	 * @param replica The replica in which to store that event.
	 * @throws IOException If it cannot be guaranteed that the event has been stored.
	 */
	void store(Event event, int replica) throws IOException;
	
	/**
	 * Returns all events of a certain type and key.
	 * 
	 * @param type The type of the desired events.
	 * @param key The key of the desired events.
	 * @param replica The replica to query.
	 * @return An Iterable of all events matching the type and key. The object only implements iterator().
	 * @throws IOException If the query was not successful.
	 */
	Iterable<Event> get(String type, byte[] key, int replica) throws IOException;
	
	/**
	 * Returns all events related to ev, in the sense that their keys have the same value, and their types are related to ev.getType().
	 * 
	 * @param ev The event for which we are interested in related events.
	 * @param replica The replica to query.
	 * @return An Iterable, providing an iterator on the events.
	 * @throws IOException If the query was not successful.
	 */
	Iterable<Event> getRelated(Event ev, int replica) throws IOException;
	
	/**
	 * Returns all the keys stored in a replica of a shard.
	 * 
	 * @param shard The shard to be queried.
	 * @param replica The replica to be queried.
	 * @return An Iterable that provides an iterator over the keys.
	 * @throws IOException If something went wrong.
	 */
	Iterable<byte[]> scanKeys(int shard, int replica) throws IOException;
	
	/**
	 * Performs arbitrary maintenance activities on a replica of a shard. 
	 * This function should be called occasionally for each value of replica and shard within the range.
	 * Maintenance operations may include the atomic removal of pairs of events that cancel each other out,
	 * or any other activity to help the store operate.
	 * 
	 * @param shard The shard in which to operate.
	 * @param replica The replica on which to operate.
	 * @throws IOException In case the activity went wrong.
	 */
	void maintenance(int shard, int replica) throws IOException;
}
