package axiom.event_store;

/**
 * Represents an event-store service.
 * This interface abstracts over the event representation, represented by the EventDomain interface.
 */
public interface EventStoreService {
	/**
	 * Create an EventStore specific to an EventDomain
	 * @param domain Represents the concrete event representation.
	 * @return An EventStore that handles events of the given domain.
	 */
	EventStore createEventStore(EventDomain domain);
}
