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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.api.IDestructor;
import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.types.SignalEvent;

//--------------------------------------------------------------
/**
 * This abstract factory manages the creation and reference requests of all factories. 
 * 
 * Using this factory will result in a reference kept for every factory created, and 
 * serves all sub-classed factories by providing base functionality common to all factories.
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 * @param <T> The type of factory
 * 
 */
//--------------------------------------------------------------
public abstract class AbstractFactory<T extends ISignalListener> implements IDestructor {
	
	/**
	 * The list of factories that have been constructed and that are still active 
	 */
	private static final List<AbstractFactory> FACTORIES = new ArrayList<>();
	
	/**
	 * The history of all factory resources that have been created
	 */
	private final Map<Class, List<T>> _history = new HashMap<>();
	
	/**
	 * The list of resources that are publicly available
	 */
	private final List<T> _resources = new ArrayList<>();
	
	public static final void reset() {
		for(AbstractFactory factory : FACTORIES) {
			factory.flush();
		}
	}
	
	//--------------------------------------------------------------
	/**
	 * Checks if the factory is running 
	 * 
	 * @return Flag indicating if the factory is running
	 */
	//--------------------------------------------------------------
	public static boolean isRunning()
	{
		return FACTORIES.size() > 0;
	}
	
	//--------------------------------------------------------------
	/**
	 * Retrieves the specified factory in question. 
	 * 
	 * If the factory does not exist then there will be an attempt 
	 * to create the factory and store a reference to it within 
	 * this construct. 
	 * 
	 * Future calls to this method will return a reference to the 
	 * created factory.
	 * 
	 * @param factoryClass The class to lookup in the 
	 * 		  list of already created factories
	 * 
	 * @return The factory of the specified concrete type
	 */
	//--------------------------------------------------------------
	public static final <T extends AbstractFactory> T getFactory(Class<T> factoryClass) {
		// Verify if the factory has already been created, if so then
		// return that instance
		for(AbstractFactory factory : FACTORIES) {
			if(factory.getClass() == factoryClass) {
				return (T) factory;
			}
		}
		
		// If execution gets to here, then it is assumed that 
		// the factory being asked for has not been created yet 
		T factory = null;
		
		try {	
			// Call the default constructor for the factory creation
			factory = factoryClass.getConstructor().newInstance();
			
			// add the factory to the factories list so that 
			// the reference is returned next time
			FACTORIES.add(factory);
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}	
		
		// Return the newly created factory reference
		return factory;
	}
	
	//--------------------------------------------------------------
	/**
	 * Helper method to add the created resource for retrieval
	 *  
	 * @param resource The resource that was created to be added
	 * @param isShared If the resource should be available publicly
	 * 
	 */
	//--------------------------------------------------------------
	private final <U extends T> void add(U resource, boolean isShared) {
		
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
	
	//--------------------------------------------------------------
	/**
	 * Gets the specified concrete class based on the type provided
	 * 
	 * @param resourceClass The class of the resource to get
	 * 
	 * @return The concrete class of the specified type
	 */
	//--------------------------------------------------------------
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
	
	//--------------------------------------------------------------
	/**
	 * Gets the specified concrete class based on type provided. 
	 * Note: This will create the resource if it does not 
     *       already exist. 
	 * 
	 * @param resourceClass The class of the resource to get
	 * @param isShared If the resource should be shared when created
	 * @param resourceParameters The arguments to be passed into 
	 * 							 the resource class
	 * 
	 * @return The created or already created resource
	 */
	//--------------------------------------------------------------
	public <U extends T> U get(Class<U> resourceClass, boolean isShared, Object... resourceParameters) {
		
		// If the shared flag is set then make sure it doesn't 
		// already exist before creating it
		if(isShared) {

			// Try to get the resource to see if it already exists
			U resource = get(resourceClass);
			if(resource != null) {
				return resource;
			}
		}

		// Construct the list of arguments that were provided
		// Note: This is used to pass into the proper constructor
		Class[] argsClass = new Class[resourceParameters.length];
		for(int i = 0; i < resourceParameters.length; ++i) {
			argsClass[i] = resourceParameters[i].getClass();
		}
		
		// Create a fresh instance of the class specified
		U createdClass = null;
		
		try {
			// Attempt to create the class by getting the constructor with the 
			// same number of arguments as the list of resourceParameters passed in
		    createdClass = resourceClass.getConstructor(argsClass).newInstance(resourceParameters);
		    
		    // Add the newly created resource into the list of 
		    // created resources so that it can be referenced 
		    // later on
			add(createdClass, isShared);
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
			
		return createdClass;
	}
	
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
	 * Gets the total count of resources from the factory
	 * 
	 * Note: This goes through the history list therefore this will
	 * include everything that was ever created, minus whatever resources
	 * have been destroyed (nullified)
	 *  
	 * @return The total number of current resources within the
	 *         history structure
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
		
	@Override public void flush() {
		_resources.clear();
		_history.clear();	
	}
	
	@Override public void dispose() {
		// TODO - determine if its necessary to call dispose
	    // on every item in the list first and then flush
	}
}