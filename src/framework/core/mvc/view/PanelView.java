package framework.core.mvc.view;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import framework.api.IView;
import framework.communication.internal.signal.arguments.EventArgs;
import framework.core.graphics.IRenderable;
import framework.core.graphics.IRenderer;

/**
 * This class represents a custom panel class for adding renderable data to the view
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class PanelView extends JPanel implements IView, IRenderer {
    
    /**
     * The extends for rendering this view
     */
    protected RendererProperties extents = new RendererProperties();
    
    /**
     * The view properties of this view
     */
    private final ViewProperties _properties = new ViewProperties(this);

    /**
     * The list of IRenderable content that can be draw to the panel
     */
    private final List<IRenderable> _renderCache = new ArrayList();

    private boolean _isHighlighted;
    
    private boolean _isForceRendering;
    
    /**
     * Adds renderable content to the queue of content to be rendered
     * 
     * @param content The content to render
     */
    protected final void addRenderableContent(IRenderable content) {
        if(content != null && !_renderCache.contains(content)) {
            _renderCache.add(content);    
        }
    }
    
    protected List<IRenderable> getRenderableContent() {
        return new ArrayList<IRenderable>(_renderCache);
    }

    public void setIsHighlighted(boolean isHighlighted) {
        _isHighlighted = isHighlighted;
        repaint();
    }
    
    public boolean getIsHighlighted() {
        return _isHighlighted;
    }
    
    protected final void setIsForceRendering(boolean isForceRendering) {
        _isForceRendering = isForceRendering;
    }
    
    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if(_isForceRendering) {
            // Force the render method that holds an image
            render(null, graphics);
        }
        else if(!this._renderCache.isEmpty()) {
        	for(IRenderable content : this._renderCache.toArray(new IRenderable[0])) {
                if(content != null) {
                    this.render(content, graphics);
                }
            }
        }
    }
    
    @Override public void render(IRenderable renderableData, Graphics context) {
        
        // Reset the paint mode
        context.setPaintMode();
       
        // Get the image associated to the renderable data
        Image image = null;
        if(renderableData != null) {
            image = renderableData.getRenderableContent();
        }
        
        // Preprocess the renderable data and the associated context
        preProcessGraphics(renderableData, context);
        
        // Calculate the values for position and width/height
        int x = extents.x;
        if(extents.x == RENDER_LIMIT_DEFAULT_VALUE) {
            x = 0;
        }
        
        int y = extents.y;
        if(extents.y == RENDER_LIMIT_DEFAULT_VALUE) {
            y = 0;
        }
        
        int width = extents.width;
        if(extents.width == RENDER_LIMIT_DEFAULT_VALUE) {
            width = getWidth();
        }
        
        int height = extents.height;
        if(extents.height == RENDER_LIMIT_DEFAULT_VALUE) {
            height = getHeight();
        }
        
        // Use the context to draw the image
        context.drawImage(image, x, y, extents.canDraw ? width : 0, extents.canDraw ? height : 0, null);
    }

    @Override public void update(EventArgs event) {
        if(!getViewProperties().shouldAlwaysRedraw()) {
            // If the view is set to be redrawn then do not clear the cached render contents
            if(!getViewProperties().shouldRedraw()) {
                _renderCache.clear();
            }

            // Set the flag back to be redrawn the next time around
            getViewProperties().setRedraw(false);
        }
    }

    @Override public final ViewProperties getViewProperties() {
        return _properties;
    }
    
    @Override public String toString() {
        return this.getClass().toString();
    }
}