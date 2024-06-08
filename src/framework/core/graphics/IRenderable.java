package framework.core.graphics;

import java.awt.Image;

/**
 * This interface defines methods for components which have resources that they would like rendered.
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public interface IRenderable {

    /**
     * Gets the renderable content provided by the implementor
     * 
     * @return The renderable content as an image
     */
    public Image getRenderableContent();
    
    /**
     * Helper method to quickly cast back to this interface type
     *
     * @return The interface type
     */
    default public IRenderable toRenderable() {
    	return (IRenderable)this;
    }
    
    default public RendererProperties getRenderableProperties() {
    	return new RendererProperties();
    }
}