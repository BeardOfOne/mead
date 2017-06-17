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

package engine.core.option;

import java.awt.event.ActionEvent;

import engine.core.mvc.controller.BaseController;

/**
 * Interface that defines the functionality that must be provided by menu options
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public interface IOption {
	
	/**
	 * Gets the visibility of the option
	 * 
	 * @return TRUE if the option is visible, FALSE if the option is not visible
	 */
	public boolean visibility();
	
	/**
	 * Gets if the option is enabled
	 * 
	 * @return TRUE if the option is enabled, FALSE if the option is not enabled
	 */
	public boolean enabled();
	
	/**
	 * Defines a method for handling an execution of the option
	 * 
	 * @param actionEvent The action event associated to the call of this method
	 */
	public void onExecute(ActionEvent actionEvent);

	/**
	 * Binds the specified option to a particular controller
	 * 
	 * @param controller The controller to bind 
	 */
	public void bind(BaseController controller);
}