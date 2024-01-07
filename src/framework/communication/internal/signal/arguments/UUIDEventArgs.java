package framework.communication.internal.signal.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import framework.api.IModel;

/**
 * Event that specifies a UUID
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 * 
 */
public final class UUIDEventArgs extends ModelEventArgs {
    /**
     * The list of unique identifiers
     */
    private final List<UUID> _identifiers = new ArrayList();
        
    /**
     * Constructs a new instance of this class type
     *
     * @param sender The sender source
     * @param operationName The name of the operation being performed
     * @param uuid The uuid to include in the event
     */
    public UUIDEventArgs(IModel sender, String operationName, UUID... uuid) { 
        super(sender, operationName);
        _identifiers.addAll(Arrays.asList(uuid));
    }
    
    /**
     * Gets the list of unique identifiers associated to this class
     *
     * @return The list of unique identifiers
     */
    public List<UUID> getIdentifiers() {
        return new ArrayList<UUID>(_identifiers);
    }
}