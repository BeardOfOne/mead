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

package engine.core.mvc.controller;

import engine.api.IController;
import engine.api.IView;

/**
 * Top-level controller class that holds common controller information
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public abstract class BaseController implements IController  {

	/**
	 * The controller properties associated to this controller
	 */
	private final ControllerProperties _properties = new ControllerProperties();
	
	/**
	 * Constructs a new instance of this class type
	 */
	public BaseController() {
	}
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param view The view associated to this controller
	 * @param <T> A type extending any IView implemented class
	 */
	@Deprecated
	public <T extends IView> BaseController(T view) {
		_properties.setEntity(view);
	}
	
	/**
	 * Gets the view associated to this controller
	 * 
	 * @return The view associated to this controller
	 */
	protected final IView getView() {
		return _properties.getEntity();
	}
	
	@Override public final ControllerProperties getControllerProperties() {
		return _properties;
	}

	@Override public boolean flush() {
		clearSignalListeners();
		return _properties.flush();
	}
}