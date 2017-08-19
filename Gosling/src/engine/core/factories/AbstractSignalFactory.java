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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;

import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.arguments.SignalEventArgs;
import engine.utils.io.logging.Tracelog;

/**
 * Factory that can communicate between signal types
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 * @param <T> Any type extending from the ISignalListener interface
 */
public abstract class AbstractSignalFactory<T extends ISignalListener> extends AbstractFactory {
	
	/**
	 * The history of all factory resources that have been created
	 */
	final Map<Class, List<T>> _history = new HashMap<>();
	
	/**
	 * The cache holds items that have been pushed into the factory but that do not
	 * currently have any association.  These items will take precedence over the creation
	 * of new items first.
	 */
	final Map<Class, Queue<T>> _cache = new HashMap<>();
	
	/**
	 * The queue of resources that are publicly available
	 */
	final List<T> _resources = new ArrayList<>();
	
	/**
	 * Gets the specified concrete class based on the type provided
	 * 
	 * @param resourceClass The class of the resource to get
	 * @param <U> A type extending The class template type 
	 * 
	 * @return The concrete class of the specified type
	 */
	public final <U extends T> U get(Class<U> resourceClass) {
		
		// Go through all the resources and try to find
		// the concrete class with the same resource class 
		for(T resource : _resources) {
			if(resource.getClass() == resourceClass) {
				return (U)resource;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the specified concrete class based on type provided. 
	 * Note: This will create the resource if it does not 
     *       already exist. 
	 * 
	 * @param resourceClass The class of the resource to get
	 * @param isShared If the resource should be shared when created
	 * @param resourceParameters The arguments to be passed into 
	 * 							 the resource class
	 * @param <U> A type extending The class template type
	 * 
	 * @return The created or already created resource
	 */
	public <U extends T> U get(Class<U> resourceClass, boolean isShared, Object... resourceParameters) {
		
		// Attempt to get a cached resource, this has priority over
		// every other resource
		U cachedResource = getCachedResource(resourceClass);
		
		// Make sure it exists
		if(cachedResource != null) {
			
			// Add it to our factory 
			add(cachedResource, isShared);
			
			// Return the resource
			return cachedResource;
		}
		
		// If the shared flag is set then make sure it doesn't 
		// already exist before creating it
		if(isShared) {

			// Try to get the resource to see if it already exists
			U resource = get(resourceClass);
			if(resource != null) {
				return resource;
			}
		}
		
		// Create a fresh instance of the class specified
		U createdClass = null;

		// Construct the list of arguments that were provided
		// Note: This is used to pass into the proper constructor
		Class[] argsClass = new Class[resourceParameters.length];
		for(int i = 0; i < resourceParameters.length; ++i) {
			argsClass[i] = resourceParameters[i].getClass();
		}
		
		try {
			
			// Attempt to create the instance with the exact match constructor
			createdClass = resourceClass.getConstructor(argsClass).newInstance(resourceParameters);
		}
		catch(Exception exception) {
			Tracelog.log(Level.WARNING, false, "No exact matching constructor found, attempting to find alternatives...");
		}
		
		// If the exact match constructor was not successful, then try to find a better constructor
		if(createdClass == null) {
			try {
				
				// Go through the list of constructors to find another match
				Constructor<U>[] constructors = (Constructor<U>[]) resourceClass.getConstructors();
				for(Constructor<U> constructor : constructors) {
					
					// Make sure that the constructor count matches, else this is not the valid constructor
					Class[] params = constructor.getParameterTypes();
					if(params.length != argsClass.length) {
						continue;
					}
					
					// Check to make sure that all parameters can be properly
					// invoked implicitly.
					// TODO This could have some serious side effects that need to be properly tested
					boolean isValid = true;
					for(int i = 0; i < params.length; ++i) {
						
						// If the parameter cannot be properly assigned from what was passed into
						// to the parameter that is being accepted at the destination, then don't
						// count the constructor currently being iterated upon
						if(!params[i].isAssignableFrom(argsClass[i])) {
							isValid = false;
							break;
						}
					}
					
					// If the constructor is a valid constructor then use that one
					if(isValid) {
						createdClass = constructor.newInstance(resourceParameters);
						
						// Add the newly created class into the factory for later reference
						add(createdClass, isShared);
						
						// Now that a constructor was found, we need to break out of the loop
						break;
					}
				}
			}
			catch(Exception exception) {
				Tracelog.log(Level.SEVERE, false, "Could not get the specified resource from the factory");				
			}
		}
		
		// Add the newly created class into the factory for later reference
		if(createdClass != null) {
			add(createdClass, isShared);			
		}

		// return the newly created class
		return createdClass;
	}

	/**
	 * Helper method to add the created resource for retrieval
	 *  
	 * @param resource The resource that was created to be added
	 * @param isShared If the resource should be available publicly
	 * @param <U> A type extending The class template type
	 * 
	 */
	private <U extends T> void add(U resource, boolean isShared) {
		
		// Get the list of classes based on the type of controller 
		// that is being added
	    List resources = _history.get(resource.getClass());
	    
	    // If there is no entry then create a new entry and add to it
	    if(resources == null) {
	    	
	    	// Create a new list and populate it with the specified resource
	        resources = new ArrayList<>();
	        resources.add(resource);
	        
	        // Add the new entry into the history structure
	        // for future reference
	        _history.put(resource.getClass(), resources);
	    }
	    // There was in fact an entry which means the resource 
	    // needs to be added into the list if it does not already
	    // exist
	    else if(!resources.contains(resource)){
	    	resources.add(resource);
	    }
	    
	    // If the resource is marked to be shared then it is
	    // stored in a separate list 
	    if(isShared) {
	        _resources.add(resource);
	    }
	}

	/**
	 * Attempts to the a cached resource 
	 * 
	 * @param resourceClass The class of the cached resource
	 * @param <U> A type extending The class template type
	 * 
	 * @return A cached resource if it exists
	 */
	private <U extends T> U getCachedResource(Class<U> resourceClass) {
		
		// Get the cache list based on the specified resource class
		Queue<T> cachedResources = _cache.get(resourceClass);
		
		T resource = null;
		
		// If the cached resources exists 
		if(cachedResources != null) {
				
			// If there are items to get
			if(cachedResources.size() > 0) {
				
				// Remove the item from the queue and get its reference
				resource = cachedResources.remove();
			
				// Logging information
				System.out.println("Info: Queue'd item " + resourceClass.getCanonicalName() + " being used, items left in queue = " + cachedResources.size());				
			}
			
			// If there are no more entries then just remove the key, it is no longer needed
			if(cachedResources.isEmpty()) {
				System.out.println("Info: No more entries in cache for " + resourceClass.getCanonicalName() + ", removing associated key");
				
				// Remove the no longer needed entry
				_cache.remove(resourceClass);			
			} 		
		}
		
		return resource != null ? (U)resource : null;
	}
	
	/**
	 * Removes the specified resource from this factory.  This will remove
	 * the reference from both the history list and shared list.
	 * 
	 * @param resource The resource to remove
	 * @param <U> A type extending The class template type
	 * 
	 */
	// TODO - remove this and have the resource implement IDestructor and handle it from there?
	public final <U extends T> void remove(U resource) {
		
		// Get the list of resources based on the class type
		List<T> resources = _history.get(resource.getClass());
		if(resources != null) {
			
			// Attempt to remove the reference from the history
			resources.remove(resource);
		}
		
		// If the reference also exists in the shared space then remove it
		// from there as well.
		_resources.remove(resource);
	}
	
	/**
	 * Gets the total count of resources from the factory
	 * 
	 * Note: This goes through the history list therefore this will
	 * include everything that was ever created, minus whatever resources
	 * have been destroyed (nullified)
	 *  
	 * @return The total number of current resources within the history structure
	 */
	protected final int getTotalResourcesCount() {
		
		int count = 0;
		
		// For every list within the history structure
		// get the total number of entries
		for(List list : _history.values()) {
			count += list.size();
		}
		
		return count;
	}
	
	/**
	 * Queues the specified resources into the factory
	 * 
	 * @param resourceClass The class type of the resource
	 * @param resources The list of resources
	 * @param <U> A type extending The class template type
	 */
	public final <U extends T> void queueResources(Class<U> resourceClass, List<U> resources) {
		// Get the list of queue'd resources based on the resource class
		Queue<T> cachedResources = _cache.get(resourceClass);
		
		// If there is no entry
		if(cachedResources == null) {
			
			// Create a new entry
			cachedResources = new LinkedList<>();
			
			// Insert into the cache the resourceClass and the cached resources empty list
			_cache.put(resourceClass, cachedResources);
		}
		
		// Populate the list of items using the reference
		cachedResources.addAll(resources);		
	}
	
	/**
	 * Pushes the queue'd resources for the specifies resource class, and returns its contents to the caller
	 * 
	 * 
	 * @param resourceClass The class type of the resource
	 * @param <U> A type extending The class template type
	 * 
	 * @return The list of resources that were pushed
	 */
	public final <U extends T> List<U> pushQueuedResources(Class<U> resourceClass) {
		
		// Get the list of queue'd resources
		Queue<T> queuedResources = _cache.remove(resourceClass);
		
		// If there are queue'd elements
		if(queuedResources != null) {
			
			// Get the reference to the list in the history field
			List<T> resources = _history.get(resourceClass);
			
			// If there is none, then create one
			if(resources == null) {
				
				// Create a new list and put the queue'd contents in it
				resources = new ArrayList(queuedResources);
				
				// Apply the array list into the history field
				_history.put(resourceClass, resources);
			} 
			else {
				
				// There is an entry, just append the queue'd contents
				resources.addAll(queuedResources);
			}
			
			// Return a new list, I do not want to give a reference to the 
			// actual list within the factory
			return new ArrayList(queuedResources);
		}
		
		return null;
	}
	
	/**
	 * Sends out a signal to a group of the specified types in a multi-cast fashion
	 * 
	 * @param classType The type of class to send the event to
	 * @param event The event to pass in, this is a signal event or one of its derived types
	 * @param <U> A type extending The class template type
	 * @param <V> A type extending The class SignalEvent
	 * 
	 */
	public final <U extends T, V extends SignalEventArgs> void multicastSignal(Class<U> classType, V event) {
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
	public final <U extends T, V extends SignalEventArgs> void multicastSignal(Collection<U> resources, V event) {
		if(resources != null) {
			for(T resource : resources) {
				// TODO - parallelize this!
				// Send out a unicast signal to every resource, although 
				// horribly inefficient the way it is being done right now
				resource.unicastSignalListener(event);
			}			
		}
	}
	
	@Override public boolean flush() {
		_history.clear();
		_resources.clear();
		_cache.clear();
		
		return true;
	}
}