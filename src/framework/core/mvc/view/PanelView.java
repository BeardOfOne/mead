/**
 * Daniel Ricci <thedanny09@icloud.com>
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

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
 * This class represents a custom panel class that ties into the gosling MVC design pattern
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
        else {
            for(IRenderable content : _renderCache) {
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