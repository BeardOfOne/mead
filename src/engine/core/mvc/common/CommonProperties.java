/**
 * Daniel Ricci <thedanny09@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package engine.core.mvc.common;

import java.util.HashMap;
import java.util.Map;

import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.SignalListenerContainer;
import engine.core.mvc.IDestructor;

/**
 * This class provides common properties that are used to query between common
 * API interface types such as a view to a controller, etc.
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 * 
 */
public class CommonProperties<T extends ISignalListener> implements IDestructor {

    /**
     * The listener entity
     */
    private T _listener;

    /**
     * The mapping of signal names to signal implementations
     */
    private final Map<String, SignalListenerContainer> _listeners = new HashMap<>();

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

    @Override public void clear() {
        _listeners.clear();
    }

    @Override public void destructor() {
    }
}