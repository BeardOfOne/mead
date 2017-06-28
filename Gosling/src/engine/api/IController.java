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

import java.util.HashMap;
import java.util.Map;

import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.ISignalReceiver;

/**
 * This is the top-most controller interface, for all sub-type implemented controller types
 * and interfaces
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public interface IController extends IDestructor, ISignalListener {
	
	/**
	 * Gets the controller properties associated to the type
	 * 
	 * @return The controller properties
	 */
	public ControllerProperties getControllerProperties();
	
	/**
	 * This class represents the controller properties of each IController implemented type
	 * 
 	 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
	 *
	 */
	public final class ControllerProperties implements IDestructor {
		
		/**
		 * The mapping of signal names to signal implementations
		 */
		private final Map<String, ISignalReceiver> _signalListeners = new HashMap<>();
		
		/**
		 * The IView associated to the IController
		 */
		private IView _view;
		
		/**
		 * Sets the specified IView in this property object
		 * 
		 * @param view The specified view to set in this properties object
		 */
		public final void setView(IView view) {
			this._view = view;
		}
		
		/**
		 * Gets the specified IView of this property object
		 * 
		 * @return The view
		 */
		public IView getView() {
			return _view;
		}
		
		/**
		 * Gets the specified IView of this property object
		 * 
		 * @param viewType The concrete type to cast the view
		 * @param <T> Any type extending IView
		 * 
		 * @return A concrete type of the view associated to this property object 
		 */
		public <T extends IView> T getView(Class<T> viewType) {
			return (T)getView();
		}
		
		/**
		 * Checks if the view is in a visible state
		 * 
		 * @return TRUE if the view is visible, FALSE if the view is not visible
		 */
		public boolean isViewVisible() {
			return getView().getContainerClass().isVisible();
		}
		
		/**
		 * Gets the signal listeners associated to this property object
		 * 
		 * @return The signal listeners
		 */
		public Map<String, ISignalReceiver> getSignalListeners() {
			return _signalListeners;
		}

		@Override public void flush() {
			_view = null;
			_signalListeners.clear();
		}
		
		@Override public void dispose() {
			_view.dispose();
		}
	}
	
	@Override default Map<String, ISignalReceiver> getSignalListeners() {
		return getControllerProperties().getSignalListeners();
	}
}