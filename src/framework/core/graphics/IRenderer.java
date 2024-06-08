package framework.core.graphics;

import java.awt.Graphics;

/**
 * This interface defines methods for components that provide the ability to render content
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public interface IRenderer {
    
    /**
     * Renders the specified renderable data using the specified graphics context
     * 
     * @param renderableData The renderable data
     * @param context The graphics context
     */
    public void render(IRenderable renderableData, Graphics context);
   
    /**
     * A hook into the graphics context and the renderable data before the draw call is initiated
     *
     * @param renderableData The renderable data entity
     * @param context The graphics context
     */
    default public void preprocessGraphics(IRenderable renderableData, Graphics context) {
    }
}