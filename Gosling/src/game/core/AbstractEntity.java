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

import java.util.ArrayList;
import java.util.List;

import engine.api.IData;
import engine.core.factories.AbstractFactory;
import engine.core.factories.DataFactory;
import game.api.IRenderable;

/**
 * Top-most class for dealing with data as an entity within the gameplay framework
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
public abstract class AbstractEntity<T extends IData> implements IRenderable {
	
	/**
	 * The data associated to the data entity
	 */
	private final List<T> _data = new ArrayList();
	
	protected final void addData(String layerName, String dataName) {
		_data.add((T) AbstractFactory.getFactory(DataFactory.class).getByName(layerName, dataName));
	}
	
	protected final void addData(String layerName) {
		_data.addAll(AbstractFactory.getFactory(DataFactory.class).getByLayer(layerName));
	}
	
	protected final int getDataSize() {
		return _data.size();
	}
	
	protected abstract String getLayerName();
	
	protected abstract List<String> getDataNames();
}