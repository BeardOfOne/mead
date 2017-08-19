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

package engine.communication.internal.signal.factory;

import engine.api.IController;
import engine.api.IModel;
import engine.api.IView;
import engine.communication.internal.signal.arguments.ControllerEventArgs;
import engine.communication.internal.signal.arguments.ModelEventArgs;
import engine.communication.internal.signal.arguments.NullEventArgs;
import engine.communication.internal.signal.arguments.SignalEventArgs;
import engine.communication.internal.signal.arguments.ViewEventArgs;

/**
 * Factory that serves SignalEvent type objects
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class SignalEventFactory {
	
	/**
	 * Not used, because this is a factory that serves signal events
	 */
	private SignalEventFactory(){
	}
	
	/**
	 * Gets a SignalEvent from the factory based on the specified parameters
	 * 
	 * @param sender The source of where this entity occurs
	 * @param operationName The name of the operation that will be performed, this is what others will listen for
	 * @param <T> A type extending the class {@link Object}
	 * 
	 * @return A SignalEvent based on the specified parameters
	 */
	public static <T extends Object> SignalEventArgs<T> getSignalEvent(T sender, String operationName) {

		SignalEventArgs event = new NullEventArgs();
		
		if(sender != null && operationName != null && operationName.trim().length() > 0) {
			if(sender instanceof IModel) {
				event = new ModelEventArgs<IModel>((IModel)sender, operationName);
			}
			else if(sender instanceof IController) {
				event = new ControllerEventArgs<IController>((IController)sender, operationName);
			}
			else if(sender instanceof IView) {
				event = new ViewEventArgs<IView>((IView)sender, operationName);
			}
			else {
				event = new SignalEventArgs<Object>(sender, operationName);
			}
		}
			
		return event;
	}
}