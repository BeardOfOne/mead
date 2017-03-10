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
 */
public interface IReceiver {
	/**
	 * Executes the specified operation based on the operations registered by the IReceivable entity
	 * 
	 * @param sender The sender
	 * @param operation The operation
	 */
	default public void executeRegisteredOperation(Object sender, String operationName) {
		Map<String, ActionListener> operations = getRegisteredOperations();
		ActionListener event;
		if(operations != null && (event = operations.get(operationName)) != null) {
			event.actionPerformed(new ActionEvent(sender, 0, null));	
		}
	}
	
	/**
	 * Gets the list of registered operation by the entity
	 * 
	 * @return Map<DispatcherOperation, ActionListener>
	 */
	default public Map<String, ActionListener> getRegisteredOperations() {
		return null;
	}
}