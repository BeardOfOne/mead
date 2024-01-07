package framework.communication.internal.signal.arguments;

import framework.api.IModel;

/**
 * Events specific from an IModel source
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class ModelEventArgs extends EventArgs {
    /**
     * Constructs a new signal type event
     * 
     * @param sender The sender source
     * @param operationName The name of the operation being performed
     */
    public ModelEventArgs(IModel sender, String operationName) { 
        super(sender, operationName);
    }
}