package framework.core.graphics;

import java.awt.Image;

/**
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class RendererProperties {
    /**
     * The default render limit value for this panel
     */
    public static final int RENDER_LIMIT_DEFAULT_VALUE = -1;
	
    /**
     * The x-coordinate for rendering 
     */
    public int x = -1;
    
    /**
     * The y-coordinate for rendering
     */
    public int y = -1;
    
    /**
     * The width for rendering
     */
    public int width = -1;
    
    /**
     * The height for rendering
     */
    public int height = -1;

    /**
     * The updated renderable image that would override everything else if set
     */
    public Image renderData;
    
    /**
     * Indicates if the renderer can perform a draw call
     * 
     * Note: If the draw call cannot be performed, then it is
     *       up to the renderer implementation to detail what should
     *       happen.
     */
    public boolean canDraw = true;
}
