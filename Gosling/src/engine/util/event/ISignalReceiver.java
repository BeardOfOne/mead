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
import java.util.Map.Entry;

/**
 * Contract that specifies how signals are received and processed
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 */
public interface ISignalReceiver {
	
	public String UPDATE_SIGNAL = "UPDATE";
	
	default public void executeRegisteredOperation(SignalEvent signalEvent) {
		
		// If we just want to do an update then trigger the update method
		if(signalEvent.getOperationName().equalsIgnoreCase(UPDATE_SIGNAL)) {
			update(signalEvent);
		}
		// Perform regular signal operation
		else {
			Map<String, ISignalListener> operations = getRegisteredOperations();
			if(operations != null) {
				String operationName = signalEvent.getOperationName();
				for(Entry<String, ISignalListener> kvp : operations.entrySet()) {
					if(kvp.getKey().equalsIgnoreCase(operationName)) {
						kvp.getValue().signalReceived(signalEvent);
						break;
					}
				}
			}
		}
	}
	
	default public Map<String, ISignalListener> getRegisteredOperations() {
		return null;
	}
	
	default public void update(SignalEvent signalEvent) {
	}
}