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
     * The default render limit value for this panel
     */
    private final int RENDER_LIMIT_DEFAULT_VALUE = -1;
    
    /**
     * The x-coordinate for rendering
     */
    private int _x = -1;
    
    /**
     * The y-coordinate for rendering
     */
    private int _y = -1;
    
    /**
     * The width for rendering
     */
    private int _width = -1;
    
    /**
     * The height for rendering
     */
    private int _height = -1;
    
    /**
     * The view properties of this view
     */
    private final ViewProperties _properties = new ViewProperties(this);

    /**
     * The list of IRenderable content that can be draw to the panel
     */
    private final List<IRenderable> _renderCache = new ArrayList();

    private boolean _isHighlighted;
    
    /**
     * Adds renderable content to the queue of content to be rendered
     * 
     * @param content The content to render
     */
    protected final void addRenderableContent(IRenderable content) {
        if(content != null) {
            _renderCache.add(content);    
        }
    }

    /**
     * Sets the rendering limits for this view.
     *
     * @param x The x position
     * @param y The y position
     * @param width The width
     * @param height The height
     */
    protected final void setRenderLimits(int x, int y, int width, int height) {
        _x = x;
        _y = y;
        _width = width;
        _height = height;
    }
             
    public void setIsHighlighted(boolean isHighlighted) {
        _isHighlighted = isHighlighted;
        repaint();
    }
    
    public boolean getIsHighlighted() {
        return _isHighlighted;
    }
    
    @Override public void render(IRenderable renderable, Graphics context) {
        Image image = null;
        if(renderable != null) {
            image = renderable.getRenderableContent();
        }
        render(image, context);
    }

    @Override public void render(Image renderableData, Graphics context) {
        context.setPaintMode();
        preProcessGraphics(context);
        context.drawImage(
            renderableData, 
            _x == RENDER_LIMIT_DEFAULT_VALUE ? 0 : _x, 
            _y == RENDER_LIMIT_DEFAULT_VALUE ? 0 : _y,
            _width == RENDER_LIMIT_DEFAULT_VALUE ? getWidth() : _width, 
            _height == RENDER_LIMIT_DEFAULT_VALUE ? getHeight() : _height, 
            null
        );
    }

    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        for(IRenderable content : _renderCache) {
            if(content != null) {
                this.render(content, graphics);
            }
        }
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
}