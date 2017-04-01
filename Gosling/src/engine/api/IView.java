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
import java.util.HashMap;
import java.util.Map;

import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.ISignalReceiver;

/**
 * This contract specifies how views should operate within the framework. 
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 */
public interface IView extends IDestructor, ISignalReceiver {
	
	/**
	 * The view properties that each IView will have
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 */
	public final class ViewProperties implements IDestructor {
		
		/**
		 * The controller that is associated to the view
		 */
		private IController _controller;		
		
		/**
		 * Flag indicating if the view has performed a render operation
		 * Note: For this to be properly set it is imperative that the programmer
		 * call the super of the render() method or this flag will not be properly registered
		 */
		private boolean _hasRendered = false;
		
		/**
		 * The mapping of signal names to signal implementations
		 */
		private final Map<String, ISignalListener> _signalListeners = new HashMap<>();
		
		/**
		 * Sets the controller of the view property
		 * 
		 * @param controller The controller
		 */
		public final void setController(IController controller) {
			this._controller = controller;
		}
		
		/**
		 * Gets the controller associated to the view
		 *  
		 * @return The controller associated to the view
		 */
		public IController getController() {
			return _controller;
		}
		
		/**
		 * Gets the controller associated to the view
		 * Note: This method compliments getController in that it does a cast for you
		 * 
		 * @param controllerType The type of controller to cast the associating controller to
		 * 
		 * @return The controller associated to view 
		 */
		public <T extends IController> T getController(Class<T> controllerType) {
			return (T)getController();
		}
		
		/**
		 * Flags this view as being rendered at least once
		 */
		protected final void flagAsRendered() {
			_hasRendered = true;			
		}
		
		/**
		 * Gets a flag indicating if the view has been rendered at least once
		 */
		public final boolean hasRendered() {
			return _hasRendered;
		}

		/**
		 * Gets the list of signal listeners associated to the view
		 * 
		 * @return The list of signal listeners 
		 */
		public Map<String, ISignalListener> getSignalListeners() {
			return _signalListeners;
		}
		
		@Override public void dispose() {
			if(_controller != null) {
				_controller.dispose();	
			}
		}

		@Override public void flush() {
			_signalListeners.clear();
		}		
	}
		
	@Override default Map<String, ISignalListener> getSignalListeners() {
		return getViewProperties().getSignalListeners();
	}
	
	@Override default void dispose() {
	}
	
	@Override default void flush() {
	}
	
	/**
	 * Gets the containing class of the view, this is the container representation in Swing terms
	 * 
	 * @return The swing container of the view
	 */
	default public <T extends Container> T getContainerClass() {
		return (T)this;
	}
	
	/**
	 * Gets a flag indicating if the view has been rendered or not
	 * @return
	 */
	default public boolean hasRendered() {
		return getViewProperties().hasRendered();
	}
	
	/**
	 * Renders the view. This should be only called once, and you should register to the update method
	 * to receive subsequent messages thereafter
	 */
	default public void render() {
		getViewProperties().flagAsRendered();
	}
	
	/**
	 * Gets a reference to the view properties associated to the view
	 * 
	 * @return the view properties associated to the view
	 */
	public ViewProperties getViewProperties();	
}