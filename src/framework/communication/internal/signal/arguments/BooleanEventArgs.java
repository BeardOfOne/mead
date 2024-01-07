package framework.communication.internal.signal.arguments;

public class BooleanEventArgs extends EventArgs {
    
    private final boolean _result;
    
    /**
     * Constructs a new signal type event
     * 
     * @param sender The sender source
     * @param operationName The name of the operation being performed
     * @param result The resulting value
     */
    public BooleanEventArgs(Object sender, String operationName, boolean result) { 
        super(sender, operationName);
        _result = result;
    }
    
    /**
     * Gets the result of this event
     *
     * @return The result of this event in boolean format
     */
    public boolean getResult() {
        return _result;
    }
}