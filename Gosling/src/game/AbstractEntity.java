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

package game;

import java.awt.Container;
import java.awt.Graphics;

import engine.api.IData;
import engine.api.IView;
import engine.core.factories.AbstractFactory;
import engine.core.factories.DataFactory;
import engine.core.mvc.model.BaseModel;
import game.api.IRenderable;

/**
 * Top-most class for dealing with data as an entity within the gameplay framework
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
public abstract class AbstractEntity<T extends IData> extends BaseModel implements IRenderable {

	/**
	 * The data associated to the data entity
	 */
	private final T _data;
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param classType The class type associated to the data
	 * @param name The name of the data entity for lookup
	 */
	protected AbstractEntity(Class<T> classType, Enum name) {
		_data = (T) AbstractFactory.getFactory(DataFactory.class).getByName(classType, name.toString());
	}
	
	@Override public void render(IView view, Graphics context) {
		
		// Get a reference to the container of the context provided
		Container container = view.getContainerClass();
		
		// Draw into the graphics of the context provided the data
		// being held in this data entity.
		context.drawImage(_data.getImageData(), 0, 0, container.getWidth(), container.getHeight(), container);
	}
}