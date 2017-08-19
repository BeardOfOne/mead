/**
* Daniel Ricci <thedanny09@gmail.com>
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

package engine.core.mvc.view;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import engine.api.IView;
import engine.communication.internal.signal.arguments.SignalEventArgs;
import game.api.IRenderable;
import game.api.IRenderer;

/**
 * This class represents a custom panel class that ties into the gosling MVC design pattern
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public abstract class PanelView extends JPanel implements IView, IRenderer {
	
	/**
	 * The view properties of this view
	 */
	private final ViewProperties _properties = new ViewProperties(this);
		
	/**
	 * The list of IRenderable content that can be draw to the panel
	 */
	private final List<IRenderable> _renderCache = new ArrayList();
	
	/**
	 * Adds renderable content to the queue of content to be rendered
	 * 
	 * @param content The content to render
	 */
	protected final void addRenderableContent(IRenderable content) {
		_renderCache.add(content);
	}
	
	/**
	 * Renders the contents currently held in the render cache
	 * 
	 * @param context The graphics context
	 */
	protected final void renderContent(Graphics context) {
		for(IRenderable content : _renderCache) {
			this.render(content, context);
		}
	}
	
	@Override public void render(IRenderable renderable, Graphics context) {
		Image image = null;
		if(renderable != null) {
			image = renderable.getRenderableContent();
		}
		
		context.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
	
	@Override protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		// Render the contents attached to the view
		renderContent(graphics);
	}

	@Override public void update(SignalEventArgs signalEvent) {
		_renderCache.clear();
	}
	
	@Override public final ViewProperties getViewProperties() {
		return _properties;
	}
}