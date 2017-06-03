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
import engine.core.factories.AbstractFactory;
import engine.core.factories.ControllerFactory;

/**
 * This is the top-most controller interface, for all sub-type implemented controller types
 * and interfaces
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public interface IController extends IDestructor, ISignalListener {
	
	/**
	 * Helper method to easily get the factory associated to this interface
	 */
	public final ControllerFactory CONTROLLER_FACTORY = AbstractFactory.getFactory(ControllerFactory.class);
	
	/**
	 * This class represents the controller properties of each IController implemented type
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 *
	 */
	public final class ControllerProperties implements IDestructor {
		
		/**
		 * The mapping of signal names to signal implementations
		 */
		private final Map<String, ISignalReceiver> _signalListeners = new HashMap<>();
		
		private IView _view;
		
		@Override public void dispose() {
			_view.dispose();
		}
		
		public final void setView(IView view) {
			this._view = view;
		}
		
		public IView getView() {
			return _view;
		}
		
		public boolean isViewVisible() {
			return getView().getContainerClass().isVisible();
		}
		
		public <T extends IView> T getView(Class<T> viewType) {
			return (T)getView();
		}

		@Override public void flush() {
			_view = null;
			_signalListeners.clear();
		}
		
		public Map<String, ISignalReceiver> getSignalListeners() {
			return _signalListeners;
		}
	}
	
	@Override default Map<String, ISignalReceiver> getSignalListeners() {
		return getControllerProperties().getSignalListeners();
	}

	public ControllerProperties getControllerProperties();
}