package axiom.event_store;

import java.io.IOException;

/**
 * Stores events.
 */
public interface EventStore {
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
	 * @param shard The shard in which to fetch associations.
	 * @param replica The replica in which to fetch associations.
	 * @return All the types that were associated with type. Regardless of whether they were given in the first or second position. The returned Iterable only provides a regular Iterator.
	 * @throws IOException If associations cannot be fetched.
	 */
	Iterable<String> getAssociation(String type, int shard, int replica) throws IOException;
	
	/**
	 * Stores one or more events in one replica. The shard is determined according to the key.
	 * All events are stored atomically, so they are either all visible, or none are.
	 * 
	 * @param events The events to be stored.
	 * @param replica The replica in which to store that event.
	 * @param timestamp A representation of the time in which the event was stored.
	 * @throws IOException If it cannot be guaranteed that the event has been stored.
	 */
	void store(Object[] events, int replica, long timestamp) throws IOException;
	
	/**
	 * Returns all events of a certain type and key. The last element may be a Continuation instance, 
	 * indicating there are more events to be retrieved.
	 * 
	 * @param type The type of the desired events.
	 * @param key The key of the desired events, as retrieved by scanKeys().
	 * @param replica The replica to query.
	 * @param since Will show results with timestamp equal or greater this value.
	 * @return An Iterable of all events matching the type and key. The object only implements iterator().
	 * @throws IOException If the query was not successful.
	 */
	Iterable<Object> get(String type, byte[] key, int replica, long since) throws IOException;
	
	/**
	 * Returns all events related to ev, in the sense that their keys have the same value, and their types are associated with the returned events' types.
	 * The last element may be a Continuation instance, indicating there are more events to be retrieved.
	 * 
	 * @param ev The event for which we are interested in related events.
	 * @param replica The replica to query.
	 * @param since Will show results with timestamp equal or greater this value.
	 * @return An Iterable, providing an iterator on the events.
	 * @throws IOException If the query was not successful.
	 */
	Iterable<Object> getRelated(Object ev, int replica, long since) throws IOException;
	
	/**
	 * After get() or getRelated() have returned a Continuation, 
	 * this function takes the continuation's binary representation (the one returned by serialize()), 
	 * and provides more events. Here too, the last element can be a Continuation, 
	 * in which case a further call to more() with its serialization can be made to retrieve further events.
	 * 
	 * @param continuation
	 * @return
	 * @throws IOException
	 */
	Iterable<Object> more(byte[] continuation) throws IOException;
	
	/**
	 * Returns all the keys stored in a replica of a shard.
	 * The keys returned by this function are not necessarily the values returned by domain.key(), 
	 * but rather some values that correspond to them, e.g., a hash value.
	 * These values must be acceptable by get().
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
	
	/**
	 * Deletes all events of a certain type from the given shard and replica.
	 * Also removes associations of that type.
	 * 
	 * @param type The type to be removed.
	 * @param shard The shard in which to perform this removal.
	 * @param replica The replica in which to perform this removal.
	 * @throws IOException In case something goes wrong.
	 */
	void pruneType(String type, int shard, int replica) throws IOException;
}
