package framework.communication.internal.signal.arguments;

import framework.api.IView;

/**
 * Events specific from an IView source
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class ViewEventArgs extends EventArgs {
    /**
     * Constructs a new signal type event
     * 
     * @param sender The sender source
     * @param operationName The name of the operation being performed
     */
    public ViewEventArgs(IView sender, String operationName) {
        super(sender, operationName);
    }	
}