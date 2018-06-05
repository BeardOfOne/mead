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

package engine.communication.internal.signal;

import java.util.Map;
import java.util.logging.Level;

import engine.communication.internal.signal.arguments.AbstractEventArgs;
import engine.core.mvc.IDestructor;
import engine.core.system.EngineProperties;
import engine.core.system.EngineProperties.Property;
import engine.utils.logging.Tracelog;

/**
 * This interface defines a methodology for communicating signals between different sub-systems using
 * a signaling concept with a custom set of event arguments
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public interface ISignalListener extends IDestructor {

    /**
     * This event is used to signal listening entities that something would like to register to you.
     */
    public static final String EVENT_REGISTER = "EVENT_REGISTER";

    /**
     * This event is used to signal listening entities that something would like to unregister from you
     */
    public static final String EVENT_UNREGISTER = "EVENT_UNREGISTER";

    /**
     * Adds a listener into the list of listeners
     * 
     * @param signalName The name of the signal 
     * @param signalReceiver The listener implementation
     */
    default public void addSignalListener(String signalName, ISignalReceiver signalReceiver) {
        Map<String, SignalListenerContainer> listeners = getSignalListeners();
        if(listeners != null && !listeners.containsKey(signalName)) {
            
            listeners.put(signalName, new SignalListenerContainer(signalName, signalReceiver));
            
            if(EngineProperties.instance().getIsPropertyInvalid(Property.SUPPRESS_SIGNAL_REGISTRATION_OUTPUT)) { 
                Tracelog.log(Level.INFO, false, String.format("Signal Registration: %s is now listening on signal %s", this.getClass().getCanonicalName(), signalName));
            }
        }
    }
    
    default public void addSignalListener(ISignalListener listener) {
    }
    
    /**
     * Clears the list of listeners
     */
    default public void clearSignalListeners() {
        Map<String, SignalListenerContainer> listeners = getSignalListeners();
        if(listeners != null) {
            Tracelog.log(Level.INFO, false, String.format("Clearing all signals for " + this.getClass().getCanonicalName()));
            listeners.clear();
        }
    }

    /**
     * Registers the specified signal name
     * 
     * @param signalName The name of the signal to register
     */
    default public void registerSignalListener(String signalName) {
        Map<String, SignalListenerContainer> listeners = getSignalListeners();
        if(signalName != null && listeners != null) {
            SignalListenerContainer container = listeners.get(signalName);
            if(container != null) {
                container.isEnabled = true;
                if(EngineProperties.instance().getIsPropertyInvalid(Property.SUPPRESS_SIGNAL_REGISTRATION_OUTPUT)) {
                    Tracelog.log(Level.INFO, false, String.format("Signal Registration: %s is now listening on signal %s", this.getClass().getCanonicalName(), signalName));
                }
            }
        }
    }

    /**
     * Removes the specified signal listener
     * 
     * @param signalName The name of the signal
     */
    default public void removeSignalListener(String signalName) {
        Map<String, SignalListenerContainer> listeners = getSignalListeners();
        if(listeners != null) {
            listeners.remove(signalName);
        }
    }

    /**
     * Sends a signal with a specified signal event to the internally defined listener
     * 
     * @param signalEvent signalEvent An event that holds information about the action being performed
     * 
     */
    default public void sendSignalEvent(AbstractEventArgs signalEvent) {
    
        // Get the list of signal listeners available
        Map<String, SignalListenerContainer> operations = getSignalListeners();
        if(operations == null) {
            return;
        }
    
        // Get the operation name
        String operationName = signalEvent.getOperationName();
    
        // Verify that the operation name is not null or empty
        if(operationName != null && !operationName.trim().isEmpty()) {
    
            // Go through every listener operation and look for the one with'
            // the matching name, then call that one 
            for(Map.Entry<String, SignalListenerContainer> kvp : operations.entrySet()) {
    
                // Verify if the key is the same as the operation name and that the signal is active
                if(kvp.getKey().equalsIgnoreCase(operationName) && kvp.getValue().isEnabled) {
                    Tracelog.log(Level.INFO, false, String.format("%s sends event %s to %s",
                            signalEvent.getSource().getClass().getSimpleName(),
                            operationName,
                            kvp.getValue().getClass().getSimpleName()
                            ));
    
                    // Send out a signal receive event
                    kvp.getValue().signalReceiver.signalReceived(signalEvent);
    
                    // Stop executing
                    break;
                }
            }
        }
    
        // Update the state of the receiver, this is done at the end to 'apply'
        // whatever changes have been made by calling events registered before-hand
        update(signalEvent);
    }

    /**
     * Unregisters the specified signal name
     * 
     * @param signalName The name of the signal to unregister
     */
    default public void unregisterSignalListener(String signalName) {
        Map<String, SignalListenerContainer> listeners = getSignalListeners();		
        if(signalName != null && listeners != null) {
            SignalListenerContainer container = listeners.get(signalName);
            if(container != null) {
                container.isEnabled = false;
                Tracelog.log(Level.INFO, false, String.format("Signal Unregistration: %s is no longer active", this.getClass().getCanonicalName()));
            }
        }
    }

    /**
     * Performs a refresh that sub-systems can hook onto to refresh their contents
     */
    @Deprecated
    default public void refresh() { }

    /**
     * Gets the list of available signal listeners that the sub-system is listening to
     * 
     * @return A mapping of listener names to listener concrete implementations
     */
    public Map<String, SignalListenerContainer> getSignalListeners();

    /**
     * An update event that sub-systems can hook onto to perform an update, similar to update loops
     * in a real-time simulation
     * 
     * @param signalEvent An event that holds information about the action being performed
     */
    public void update(AbstractEventArgs signalEvent);
    
    /**
     * Registers the handlers that will listen in for messages that are called
     */
    public void registerSignalListeners();
}