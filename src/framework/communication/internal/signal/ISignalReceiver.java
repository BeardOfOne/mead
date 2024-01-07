package framework.communication.internal.signal;

import java.util.EventListener;

import framework.communication.internal.signal.arguments.EventArgs;

/**
 * Defines the contractual interface for consuming signal events
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 * @param <T> A type extending a signal event
 */
public interface ISignalReceiver<T extends EventArgs> extends EventListener {

    /**
     * This method is invoked when a particular signal is received. This
     * signal is in the form of a particular SignalEvent
     * 
     * @param event The event container
     */
    public void signalReceived(T event);
}