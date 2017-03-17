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

package engine.util.event;

import java.util.Map;

/**
 * This interface defines a methodology for communicating signals between different sub-systems using
 * a signaling concept with a custom set of custom event arguments
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public interface ISignalReceiver {
	/**
	 * Sends a signal with a specified signal event to the internally defined listener
	 * 
	 * @param signalEvent signalEvent An event that holds information about the action being performed
	 * 
	 */
	default public void sendSignal(SignalEvent signalEvent) {
		
		/**
		 * Get the list of listening entities of this receiver, and look
		 * for the right entity to call upon.  Note that a set of entities
		 * is registered by a receiver.  An entity can be thought of as 
		 * an event.
		 */
		Map<String, ISignalListener> operations = getSignalListeners();
		if(operations != null) {
			String operationName = signalEvent.getOperationName();
			if(operationName != null && !operationName.isEmpty()) {
				for(Map.Entry<String, ISignalListener> kvp : operations.entrySet()) {
					if(kvp.getKey().equalsIgnoreCase(operationName)) {
						kvp.getValue().signalReceived(signalEvent);
						break;
					}
				}
			}
		}
		
		// Update the state of the receiver, this is done at the end to 'apply'
		// whatever changes could potentially be made from above
		update(signalEvent);
	}
	
	/**
	 * Gets the list of available signal listeners that the sub-system is listening to
	 * 
	 * @return A mapping of listener names to listener concrete implementations
	 */
	default public Map<String, ISignalListener> getSignalListeners() {
		return null;
	}
	
	/**
	 * An update event that sub-systems can hook onto to perform an update, similar to update loops
	 * in a real-time simulation
	 * 
	 * @param signalEvent An event that holds information about the action being performed
	 */
	default public void update(SignalEvent signalEvent) {
	}
}