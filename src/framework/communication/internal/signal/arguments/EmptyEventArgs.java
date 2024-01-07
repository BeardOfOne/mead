package framework.communication.internal.signal.arguments;

public final class EmptyEventArgs extends EventArgs {

    /**
     * Using a temporary object to pass as a sender
     */
    private static final Object _tempObj = new Object();
    
    /**
     * Constructs a new signal type event
     */
    public EmptyEventArgs() {
        super(_tempObj, "");
    }
}