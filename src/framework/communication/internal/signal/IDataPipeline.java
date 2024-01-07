package framework.communication.internal.signal;

/**
 * A simple interface for data exchange using IoC between two components
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 * @param <T> The type of data being piped between both components
 */
public interface IDataPipeline<T extends Object> extends ISignalListener {
    /**
     * Piped the specified data back to some caller
     * 
     * @param data The data to pass through the pipe
     */
    public void pipeData(T data);
}
