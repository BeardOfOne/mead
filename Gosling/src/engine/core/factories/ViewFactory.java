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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import engine.api.IDestructor;
import engine.api.IDispatcher;
import engine.api.IView;
import engine.communication.internal.dispatcher.Dispatcher;
import engine.communication.internal.dispatcher.DispatcherMessage;

public class ViewFactory implements IDestructor, IDispatcher<IView> {

	/**
	 * A message dispatcher used to communicate with views 
	 */
	Dispatcher<IView> _dispatcher = new Dispatcher<>();
	
	/**
     * Contains the history of all the views ever created by this factory, organized 
     * by class name and mapping to the list of all those classes
     */
    private final Map<String, Set<IView>> _history = new HashMap<>();

	private final ArrayList<IView> _views = new ArrayList<>(); 
	
	private static ViewFactory _instance;
		
	private ViewFactory() {
		_dispatcher.start();
	}
	
	/**
	 * Adds a view
	 * 
	 * @param controller The controller to add
	 * @param isShared If the controller should be added into the exposed cache
	 */
	private void Add(IView view, boolean isShared) { 
	    String viewName = view.getClass().getName();
	    
	    Set<IView> views = _history.get(viewName);
	    if(views == null) {
	        views = new HashSet<IView>();
	        _history.put(viewName, views);
	    }
	    views.add(view);
	    
	    if(isShared) {
	        _views.add(view);
	    }
	}
	
	public synchronized static ViewFactory instance() {
		if(_instance == null) {
			_instance = new ViewFactory();
		}
		return _instance;
	}
	
	public <T extends IView> IView get(Class<T> viewClass) {
		IView view = _views.stream().filter(z -> z.getClass() == viewClass).findFirst().get();
		assert view != null : "Attemted to find view " + viewClass.getName() + " but could not";
		return view;
	}
	
	public <T extends IView> IView get(Class<T> viewClass, boolean isShared, Object...args) {
		System.out.println("Attempting to get " + viewClass.getName());
		if(isShared) {
			for(IView item : _views) {
				if(item.getClass() == viewClass) {
					return item;
				}
			}
		}
		
		// Get the list of arguments together
		Class<?>[] argsClass = new Class<?>[args.length];
		for(int i = 0; i < args.length; ++i) {
			argsClass[i] = args[i].getClass();
		}
		
		try {	
			T view = viewClass.getConstructor(argsClass).newInstance(args);
			Add(view, isShared);
			return view;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return null;
	}
		
	@Override public void dispose() {
		for(IView view : _views) { 
			view.dispose();
		}
		_views.clear();
		_instance = null;
	}

	@Override public <U extends IView> void SendMessage(Object sender, String operationName, Class<U> type, Object... args) {
		List<IView> resources = null;
		
		for(Set<IView> views : _history.values()) {
			if(views.iterator().next().getClass() == type) {
				resources = new ArrayList<>(views);
				break;
			} 
			continue;
		}

		DispatcherMessage<IView> message = new DispatcherMessage<IView>(sender, operationName, resources, Arrays.asList(args));
		_dispatcher.add(message);
	}
}