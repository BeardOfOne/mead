package framework.communication.internal.signal;

/**
 * Container class used for holding signal names to signal listener relationships
 * 
 * This container is usually used when keeping track of which signals are attached to a particular
 * entity
 * 
 */
public class SignalListenerContainer {

    /**
     * The signal name 
     */
    public final String signalName;

    /**
     * The signal receiver
     */
    public final ISignalReceiver signalReceiver;

    /**
     * If this signal can be invoked on
     */
    public boolean isEnabled = true;
    
    /**
     * Constructs a new instance of this class type
     *
     * @param signalName The signal name
     * @param signalReceiver The signal receiver
     */
    public SignalListenerContainer(String signalName, ISignalReceiver signalReceiver) {
        this.signalName = signalName;
        this.signalReceiver = signalReceiver;
    }
}