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

import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.ISignalReceiver;
import engine.core.mvc.common.CommonProperties;

/**
 * This contract specifies how views should operate within the framework. 
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
public interface IView extends IDestructor, ISignalListener {
	
	/**
	 * The view properties that each IView will have
	 * 
	 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
	 */
	public final class ViewProperties extends CommonProperties<IController>  {
						
		/**
		 * Flag indicating if the view has performed a render operation
		 * Note: For this to be properly set it is imperative that the programmer
		 * call the super of the render() method or this flag will not be properly registered
		 */
		private boolean _hasRendered = false;

		/**
		 * Constructs a new view properties object in charge of holding
		 * information about the view such as the controller
		 * 
		 * @param view A reference to the container class implementing
		 * the view.
		 * 
		 * Note: The parameter exists because from an interface there is no way
		 * to get a reference to the outer class using Outer.this.methodName
		 */
		public ViewProperties(IView view) {
			
			// The view is set to false because by default most container classes
			// are already visible when they are created and because of this no event
			// will ever be sent out that a JPanel or some other container is being shown
			//
			// Note: The render at this point in time is now in charge is setting a container
			// class to visible, but the implementor must call super
			// 
			// Note: Do not touch this line of code, ever.
			view.getContainerClass().setVisible(false);
			
			// Register the view to listen in on the event when it becomes hidden/shown, this
			// is done so that registration can happen on signals
			//
			// Note: This needs to be done after setting the container's visibility to false
			// or there will be a false positive for the hidden event
			view.getContainerClass().addComponentListener(new ComponentAdapter() {
				
		        // This event is fired when a view is hidden, that is when
		        // the visibility is set to false
		        @Override public void componentHidden(ComponentEvent args) {

		        	IController controller = getEntity(IController.class);
		        	
		        	// Note: Some views don't have controllers
		        	if(controller == null) {
		        		System.out.println("Warning: Cannot invoke controller for hidden component " + args.getSource().getClass().getCanonicalName());
		        		return;
		        	}
		        	
		        	// Unregister all signals being listened to by this view's controller
		        	controller.unregisterSignalListeners();
		        	
		           	// Unregister all signals being listened to by the view
		           	view.unregisterSignalListeners();
		           	
		        	System.out.println(String.format("Component %s is being hidden", args.getSource().getClass().getCanonicalName()));
		        }
		        
		        // This event is fired when  a view is shown, that is when
		        // the visibility is set to true
		        @Override public void componentShown(ComponentEvent args) {
		        	
		        	IController controller = getEntity(IController.class);
		        	
		        	// Note: Some views don't have controllers
		        	if(controller == null) {
		        		System.out.println("Warning: Cannot invoke controller for shown component " + args.getSource().getClass().getCanonicalName());
		        		return;
		        	}
		        	
		        	// Indicate that this view is being shown now
		        	System.out.println(String.format("Component %s is being shown", args.getSource().getClass().getCanonicalName()));
		        	
		        	// If a registration already occurred then log that this is the case and don't register again
		        	// Note: This can occur if registration occurred before the actual view was about to be shown, this is common
		        	// so that is why it is an Info and not a Warning.
		        	Map listeners = controller.getSignalListeners();
		        	if(listeners == null || !listeners.isEmpty()) {
		        		System.out.println(String.format(
	        				"Info: Signal listeners already detected for %s, not registering again", 
	        				controller.getClass().getCanonicalName()
        				));
		        		
		        		return;
		        	}
		        	
		        	// Register all signals being listened to by the view's controller
		        	controller.registerSignalListeners();
		           	
		           	// Register all signals being listened to by the view
		           	view.registerSignalListeners();
		        }
		        
		    });
		}
		
		/**
		 * Flags this view as being rendered at least once
		 */
		protected final void flagAsRendered() {
			_hasRendered = true;			
		}
		
		/**
		 * Gets a flag indicating if the view has been rendered at least once
		 * 
		 * @return If the view has been rendered
		 */
		public final boolean hasRendered() {
			return _hasRendered;
		}
	}
		
	/**
	 * Gets a reference to the view properties associated to the view
	 * 
	 * @return the view properties associated to the view
	 */
	public ViewProperties getViewProperties();	
	
	/**
	 * Initializes the list of components for the view.
	 * 
	 * Note: You should not put bindings in here
	 * 
	 * Note: This is where you should put initialization of UI controls/components
	 * 
	 * Note: This is called BEFORE the initializeComponentBindings is called
	 * 
	 *  
	 */
	public void initializeComponents();
	
	/**
	 * Defines how the bindings of the components in the view are initialized
	 * 
	 * Note: This is automatically called by the corresponding 
	 *       factory. This method will be called after the 
	 *       constructor is done executing
	 * 
	 * Note: This method should only include binding specific information about a control
	 * 
	 * Note: This is not the place where you should deal with
	 *       signals from the SignalListener interface and such, 
	 *       this is really specific to the UI itself and its 
	 *       JComponent/Container hierarchies 
	 */
	public void initializeComponentBindings();
	
	/**
	 * Gets the containing class of the view, this is the container representation in Swing terms
	 * 
	 * @param <T> A type extending the class {@link Container}
	 * 
	 * @return The swing container of the view
	 */
	default public <T extends Container> T getContainerClass() {
		return (T)this;
	}
	
	/**
	 * Gets a flag indicating if the view has been rendered or not
	 * 
	 * @return If the view has been rendered
	 */
	default public boolean hasRendered() {
		return getViewProperties().hasRendered();
	}
	
	/**
	 * Renders the view. This should be only called once.
	 * 
	 * Note: You should register to the update method
	 * to receive subsequent messages thereafter
	 * 
	 * Note: To handle deferred rendering on the first
	 * pass, simply override the setVisible method and 
	 * do your pre-processing work, then call super(flag)
	 */
	default public void render() {
		// If the render call has already been made then don't do it again
		// If, however, for whatever reason the view is not visible, then 
		// try to render it again to make it visible
		if(!getViewProperties()._hasRendered || !getContainerClass().isVisible()) {
			getViewProperties().flagAsRendered();
			getContainerClass().setVisible(true);	
		}	
	}
	
	@Override default Map<String, ISignalReceiver> getSignalListeners() {
		return getViewProperties().getSignalListeners();
	}
}