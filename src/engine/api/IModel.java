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

package engine.api;

import java.util.Map;
import java.util.UUID;

import engine.communication.internal.persistance.IXMLCodec;
import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.SignalListenerContainer;
import engine.core.factories.AbstractFactory;
import engine.core.mvc.IDestructor;
import engine.core.mvc.common.CommonProperties;
import game.core.ModelFactory;

/**
 * This interface describes the general contract rules of all model type implementors 
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public interface IModel extends IDestructor, ISignalListener, IXMLCodec {
	
	/**
	 * This event is used to indicate that a listener has been added
	 * from some entity. You will receive a notifications when this 
	 * occurs.
	 */
	public static final String EVENT_LISTENER_ADDED = "LISTENER_ADDED";
	
	/**
	 * This event is used to indicate that the model listens to events that
	 * need a reference to itself.
	 */
	public static final String EVENT_PIPE_DATA = "EVENT_PIPE_DATA";
		
	/**
	 * Gets the identifier associated with the model
	 * 
	 * @return The unique identifier of the object
	 */
	public abstract UUID getUUID();
	
	/**
	 * Gets the model properties of the implementor of this interface
	 * 
	 * @return The model properties of the implementor
	 */
	public CommonProperties getModelProperties();

	/**
	 * Performs a copy of the specified model
	 * 
	 * @param model The model to copy
	 */
	public void copyData(IModel model);

	/**
	 * Removes this model from the context of the application
	 */
	default public void remove() {
		clearSignalListeners();
		AbstractFactory.getFactory(ModelFactory.class).remove(this);
		flush();
	}

	@Override default Map<String, SignalListenerContainer> getSignalListeners() {
		return getModelProperties().getSignalListeners();
	}
}