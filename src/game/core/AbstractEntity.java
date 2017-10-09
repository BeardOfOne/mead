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

package game.core;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import engine.api.IData;

import engine.core.factories.AbstractFactory;
import engine.core.factories.DataFactory;
import engine.core.mvc.model.BaseModel;
import engine.core.ui.graphics.IRenderable;
import engine.utils.io.logging.Tracelog;

/**
 * Top-most class for dealing with data as an entity within the game play framework
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
public abstract class AbstractEntity<T extends IData> extends BaseModel implements IRenderable {
	
	/**
	 * The data associated to the initially specified active data
	 */
	private final List<T> _layerData = new ArrayList();
	
	/**
	 * The layer name associated to this entity
	 */
	private final String _layerName;
	
	/**
	 * The data associated to the data entity
	 */
	private T _activeData;
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param layerName The name of the layer
	 */
	protected AbstractEntity(String layerName) {
		// Get the list of data associated to active data specified. This is
		// used so that the abstract entity has a reference to the layer data
		// for switching purposes
		_layerData.addAll(AbstractFactory.getFactory(DataFactory.class).getByLayer(layerName));
		
		_layerName = layerName;
	}
	
	/**
	 * Gets the layer name associated to this entity
	 * 
	 * @return The layer name
	 */
	protected final String getLayerName() {
	    return _layerName;
	}
	
	/**
	 * Gets the names of the data held by this entity, used for lookup purposes
	 * 
	 * @return The list of data names
	 */
	protected final List<String> getDataNames() {
		List<String> names = new ArrayList();
		for(T data : _layerData) {
			names.add(data.getName());
		}
		
		return names;
	}
	
	/**
	 * Sets the currently active data element of this entity
	 * 
	 * @param dataName The data name, one that would be retrieved if calling getDataNames for example
	 */
	protected final void setActiveData(String dataName) {
		if(dataName != null) {
			for(T data : _layerData) {
				if(data.getName().equalsIgnoreCase(dataName)) {
					Tracelog.log(Level.INFO, false, "Active data being set to " + dataName);
					_activeData = data;
					break;
				}
			}
		}
	}
	
	@Override public Image getRenderableContent() {
		return _activeData != null ? _activeData.getImageData() : null;
	}
}