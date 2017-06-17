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

package engine.core.factories;

import java.util.Collection;
import java.util.List;

import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.types.SignalEvent;

/**
 * Factory that can communicate between signal types
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 * @param <T> Any type extending from the ISignalListener interface
 */
public abstract class AbstractSignalFactory<T extends ISignalListener> extends AbstractFactory<T> {
	
	/**
	 * Sends out a signal to a group of the specified types in a multi-cast fashion
	 * 
	 * @param classType The type of class to send the event to
	 * @param event The event to pass in, this is a signal event or one of its derived types
	 * @param <U> A type extending The class template type
	 * @param <V> A type extending The class SignalEvent
	 * 
	 */
	public final <U extends T, V extends SignalEvent> void multicastSignal(Class<U> classType, V event) {
		List<T> resources = _history.get(classType);
		if(resources != null) {
			for(T resource : resources) {
				// TODO - parallelize this!
				// Send out a unicast signal to every resource, although 
				// horribly inefficient the way it is being done right now
				resource.unicastSignalListener(event);
			}			
		}
	}
	
	/**
	 * Sends out a signal to a group of the specified types in a multi-cast fashion
	 * 
	 * @param resources The list of resources to send the signal to
	 * @param event The event to pass in, this is a signal event or one of its derived types
	 * @param <U> A type extending The class template type
	 * @param <V> A type extending a signal event class
	 */
	public final <U extends T, V extends SignalEvent> void multicastSignal(Collection<U> resources, V event) {
		if(resources != null) {
			for(T resource : resources) {
				// TODO - parallelize this!
				// Send out a unicast signal to every resource, although 
				// horribly inefficient the way it is being done right now
				resource.unicastSignalListener(event);
			}			
		}
	}
}