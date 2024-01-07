package framework.core.mvc.common;

import java.util.HashMap;
import java.util.Map;

import framework.communication.internal.signal.ISignalListener;
import framework.communication.internal.signal.SignalListenerContainer;

/**
 * This class provides common properties that are used to query between common
 * API interface types such as a view to a controller, etc.
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 * 
 */
public class CommonProperties<T extends ISignalListener> {

    /**
     * The listener entity
     */
    private T _listener;

    /**
     * The mapping of signal names to signal implementations
     */
    private final Map<String, SignalListenerContainer> _listeners = new HashMap();

    /**
     * Gets the list of signal listeners associated to the view
     * 
     * @return The list of signal listeners 
     */
    public final Map<String, SignalListenerContainer> getSignalListeners() {
        return _listeners;
    }

    /**
     * Gets the entity
     * 
     * @return The entity
     */
    public final T getEntity() {
        return _listener;
    }

    /**
     * Gets the specified listener entity type as a convenience method
     * 
     * @param classType The type to get
     * @param <U> ISignalListener type
     * 
     * @return The listener as the specified type
     */
    public final <U extends T> U getEntity(Class<U> classType) {
        return _listener != null ? (U)_listener : null;
    }

    /**
     * Sets the specified entity
     * 
     * @param entity The entity
     */
     public final void setEntity(T entity) {
        this._listener = entity;
    }
}