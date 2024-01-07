package framework.communication.internal.persistance;

/**
 * Serializable interface defines top-level contractual functionality to serialize an object
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 * 
 * @param <T> A type extending the class {@link Object}
 */
public interface ISerializable<T extends Object> {	
    public T serialize();
}