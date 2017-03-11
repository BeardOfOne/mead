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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Contract that specifies how entities register to dispatched events
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 */
public interface IReceiver {
	
	/**
	 * This listener class introduces the ability to attach custom arguments
	 * to action listener types.  You must extend this class when defining 
	 * action listener events if you want to do inter-component message passing
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 *
	 */
	public abstract class ReceiverListener implements ActionListener {
		public Object[] args;
	}
	
	/**
	 * Executes the specified operation based on the operations registered by the IReceivable entity
	 * 
	 * @param sender The sender of the event
	 * @param operation The operation to fire
	 * @param args The list of arguments to include
	 */
	default public void executeRegisteredOperation(Object sender, String operationName, Object... args) {
		Map<String, ReceiverListener> operations = getRegisteredOperations();
		if(operations != null) {
			ReceiverListener event = operations.get(operationName);
			if(event != null) {
				event.args = args;		
				event.actionPerformed(new ActionEvent(sender, 0, operationName));				
			}
		}
	}
	
	/**
	 * Gets the list of registered operation by the entity
	 * 
	 * @return A mapping of operations to receiver listener types 
	 */
	default public Map<String, ReceiverListener> getRegisteredOperations() {
		return null;
	}
}