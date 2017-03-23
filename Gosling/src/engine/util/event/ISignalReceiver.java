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
		
		/*
		 * Get the list of listening events of this receiver, and look
		 * for the right one to call upon.  Note that a set of events
		 * is registered by a receiver.
		 */
		Map<String, ISignalListener> operations = getSignalListeners();
		if(operations != null) {
			String operationName = signalEvent.getOperationName();
			if(operationName != null && !operationName.trim().isEmpty()) {
				for(Map.Entry<String, ISignalListener> kvp : operations.entrySet()) {
					if(kvp.getKey().equalsIgnoreCase(operationName)) {
						System.out.println("Signal:: " + operationName + " ---> " + kvp.getValue().getClass().getCanonicalName());
						kvp.getValue().signalReceived(signalEvent);
						break;
					}
				}
			}
		}
		
		// Update the state of the receiver, this is done at the end to 'apply'
		// whatever changes have been made by calling events registered before-hand
		update(signalEvent);
	}
	
	/**
	 * Registers a listener into the list of listeners
	 * 
	 * @param signalName The name of the signal 
	 * @param listener The listener implementation
	 */
	default public void registerListener(String signalName, ISignalListener listener) {
		Map<String, ISignalListener> listeners = getSignalListeners();
		if(listeners != null) {
			if(!listeners.containsKey(signalName)) {
				listeners.put(signalName, listener);
			}
		}
	}
	
	/**
	 * Unregisters the specified listener, returning a key to use as a reference to help
	 * when you wish to register back, you will be able to use the same key.
	 * 
	 * @param listener The listener to unregister
	 * 
	 * @return The name of the key associated to the listener that you passed in originally, use
	 * this as record keeping and to register back
	 */
	default public String unregisterListener(ISignalListener listener) {
		Map<String, ISignalListener> listeners = getSignalListeners();
		if(listeners != null) {
			for(Map.Entry<String, ISignalListener> kvp : listeners.entrySet()) {
				if(kvp.getValue() == listener) {
					listeners.remove(kvp.getKey());
					return kvp.getKey();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Clears the list of listeners
	 */
	default public void unregisterListeners() {
		Map<String, ISignalListener> listeners = getSignalListeners();
		if(listeners != null) {
			listeners.clear();
		}
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
	 * Registers the handlers that will listen in for messages that are called
	 */
	default public void registerListeners() {
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