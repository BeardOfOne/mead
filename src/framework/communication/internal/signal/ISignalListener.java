/**
 * Daniel Ricci {@literal <thedanny09@icloud.com>}
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

package framework.communication.internal.signal;

import java.util.Map;
import java.util.logging.Level;

import framework.communication.internal.signal.arguments.EventArgs;
import framework.core.system.EngineProperties;
import framework.core.system.EngineProperties.Property;
import framework.utils.logging.Tracelog;

/**
 * This interface defines a methodology for communicating signals between different sub-systems using
 * a signaling concept with a custom set of event arguments
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public interface ISignalListener {

    /**
     * This event is used to signal listening entities that something would like to register to you.
     */
    public static final String EVENT_REGISTER = "EVENT_REGISTER";

    /**
     * This event is used to signal listening entities that something would like to unregister from you
     */
    public static final String EVENT_UNREGISTER = "EVENT_UNREGISTER";

    /**
     * Adds the specified signal and signal implementation to the list of available, invokable, signals
     *
     * @param signalName The name of the signal
     * @param signalReceiver The signal implementation
     */
    default public void addSignal(String signalName, ISignalReceiver signalReceiver) {
        Map<String, SignalListenerContainer> listeners = getSignals();
        if(listeners != null && !listeners.containsKey(signalName)) {
            listeners.put(signalName, new SignalListenerContainer(signalName, signalReceiver));
            if(EngineProperties.instance().getIsPropertyInvalid(Property.SUPPRESS_SIGNAL_REGISTRATION_OUTPUT)) { 
                Tracelog.log(Level.INFO, false, String.format("Signal Registration: %s is now listening on signal %s", this.getClass().getCanonicalName(), signalName));
            }
        }
    }
    
    /**
     * Clears all the signals that have been registers to this listener
     */
    default public void clearSignals() {
        Map<String, SignalListenerContainer> listeners = getSignals();
        if(listeners != null) {
            Tracelog.log(Level.INFO, false, String.format("Clearing all signals for " + this.getClass().getCanonicalName()));
            listeners.clear();
        }
    }

    /**
     * Enabled/Disables the specified signal name. When a signal is enabled, it can be invoked by callers, when it is
     * disabled then it cannot be invoked by the signal listening system.
     *
     * @param signalName The name of the signal
     * @param isEnabled TRUE if the signal is enabled, FALSE otherwise
     */
    default public void setSignalEnabled(String signalName, boolean isEnabled) {
        Map<String, SignalListenerContainer> listeners = getSignals();
        if(signalName != null && listeners != null) {
            SignalListenerContainer container = listeners.get(signalName);
            if(container != null) {
                container.isEnabled = isEnabled;
                Tracelog.log(Level.INFO, false, String.format("Signal %s is now %s", this.getClass().getCanonicalName(), isEnabled ? "enabled" : "disabled"));
            }
        }
    }

    /**
     * Removes the specied signal from the list of available, invokable, signals.
     *
     * @param signalName The name of the signal
     */
    default public void removeSignal(String signalName) {
        Map<String, SignalListenerContainer> listeners = getSignals();
        if(listeners != null) {
            listeners.remove(signalName);
        }
    }

    default public void invokeSignal(EventArgs signalEvent) {
    
        // Get the list of signal listeners available
        Map<String, SignalListenerContainer> operations = getSignals();
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
    
        if(!signalEvent.isUpdateSuppressed()) {
            // Update the state of the receiver, this is done at the end to 'apply'
            // whatever changes have been made by calling events registered before-hand
            update(signalEvent);
        }
    }

    /**
     * Gets the list of available signal that this listener has available
     * 
     * @return A mapping of signal names to signal implementations
     */
    public Map<String, SignalListenerContainer> getSignals();

    /**
     * An update event that sub-systems can hook onto to perform an update, similar to update loops
     * in a real-time simulation
     * 
     * @param signalEvent An event that holds information about the action being performed
     */
    public void update(EventArgs signalEvent);
}