package framework.communication.internal.signal.arguments;

import framework.communication.internal.signal.IDataPipeline;

/**
 * Events specific from an IController source
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class PipelinedEventArgs extends EventArgs {
    /**
     * Constructs a new signal type event
     * 
     * @param sender The sender source
     * @param operationName The name of the operation being performed
     */
    public PipelinedEventArgs(IDataPipeline sender, String operationName) {
        super(sender, operationName);
    }
}