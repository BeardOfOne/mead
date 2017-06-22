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

import java.util.List;

import engine.api.IModel;
import engine.communication.internal.signal.types.UUIDEvent;

/**
 * The model factory used to create model type resources
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class ModelFactory extends AbstractSignalFactory<IModel> {
	
	/**
	 * Performs a selective multicast on the specified class type
	 * 
	 * Note: This performs a selective multicast using the UUID of the
	 *  
	 * @param classType The class type to multicast on
	 * @param event The event to pass to each resource
	 * @param <U> A type extending The class template type
	 * @param <T> A type extending any class type
	 */
	public <U extends IModel, T extends Object> void selectiveMulticastSignal(Class<U> classType, UUIDEvent event) {

		// Get the list of models that have currently been created
		List<IModel> resources = _history.get(classType);
		
		// Verify that the list if valid
		if(resources != null) {
			
			// Unwrap the event and cast it to a model event.
			// This is a strong assumption, but right now this is what
			// the assumption is when sending out a signal using this factory.
			UUIDEvent<U> uuidEvent = event;
			
			// For every resource that has been created
			for(IModel resource : resources) {
				if(uuidEvent.Identifiers.contains(resource.getIdentifier())) {
					resource.unicastSignalListener(event);
				}
			}			
		}
	}
	
	/**
	 * Gets the list of all models of the specified class type created
	 * 
	 * @param classType The class type to lookup
	 * 
	 * @return The list of models of the specified type
	 */
	public <U extends IModel> List<U> getAll(Class<U> classType) {
		List<U> resources = (List<U>) _history.get(classType);
		return resources;
	}
}