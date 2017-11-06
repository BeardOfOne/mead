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

package game.core;

import java.util.List;
import java.util.Queue;
import java.util.logging.Level;

import engine.api.IModel;
import engine.communication.internal.signal.arguments.UUIDEventArgs;
import engine.core.factories.AbstractSignalFactory;
import engine.utils.logging.Tracelog;

/**
 * The model factory used to create model type resources
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class ModelFactory extends AbstractSignalFactory<IModel> {
	
	@Override public <U extends IModel> U add(U resource, boolean isShared) {

		// Add the actual resource
		super.add(resource, isShared);

		Queue<U> cachedResources = (Queue<U>) _cache.get(resource.getClass());
		if(cachedResources != null && !cachedResources.isEmpty()) {
			U cachedResource = cachedResources.remove();
			if(cachedResource != null) {

				// Copy the data that has been cached over to the model
				resource.copyData(cachedResource);
				
				// Perform a refresh of the model to show the updated contents
				resource.refresh();
				
				// Flush the contents of what was in the cache
				cachedResource.flush();	
				
				// Remove the cache entry if there are no more elements left
				if(cachedResources.isEmpty()) {
					Tracelog.log(Level.INFO, false, "Removing the key " + resource.getClass() + " from the cache");
					_cache.remove(resource.getClass());
				}				
			}
			else {
				Tracelog.log(Level.SEVERE, false, "Could not inject the specified model");
			}
		}
		
		return resource;
	}
	
	/**
	 * Performs a selective multicast on the specified class type
	 * 
	 * Note: This performs a selective multicast using the specified UUID
	 *  
	 * @param classType The class type to multicast on
	 * @param event The event to pass to each resource
	 * @param <U> A type extending The class template type
	 * @param <T> A type extending any class type
	 */
	public <U extends IModel, T extends Object> void selectiveMulticastSignal(Class<U> classType, UUIDEventArgs event) {

		// Get the list of models that have currently been created
		List<IModel> resources = _privateSignals.get(classType);
		
		// Verify that the list if valid
		if(resources != null) {
			
			// Unwrap the event and cast it to a model event.
			// This is a strong assumption, but right now this is what
			// the assumption is when sending out a signal using this factory.
			UUIDEventArgs<U> uuidEvent = event;
			
			// For every resource that has been created
			for(IModel resource : resources) {
				if(uuidEvent.Identifiers.contains(resource.getUUID())) {
					resource.sendSignalEvent(event);
				}
			}			
		}
	}
}