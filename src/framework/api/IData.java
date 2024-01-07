package framework.api;

import java.awt.Point;
import java.util.List;
import java.util.UUID;

/**
 * Provides a data contract for all data type objects
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public interface IData {

    /**
     * @return The identifier associated to the data
     */
    public UUID getIdentifier();
    
    /**
     * @return The name, if any, of the layer.
     */
    public List<UUID> getLayers();
    
    /**
     * @return The top-left coordinate of this data
     */
    public Point getTopLeft();
    
    /**
     * @return The bottom-right coordinate of this data
     */
    public Point getBottomRight();
}