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

package engine.core.factories;

import engine.api.IData;
import engine.communication.external.builder.DataBuilder;
import engine.communication.external.builder.Director;
import engine.core.system.EngineProperties;
import engine.core.system.EngineProperties.Property;

/**
 * Data factory for extracting data from an external source
 * based on specified types
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public final class DataFactory<T extends IData> extends AbstractDataFactory<T> {

	@Override public void loadData() {
		
		String dataPath = EngineProperties.instance().getProperty(Property.DATA_PATH_VALUE);
		if(dataPath == null || dataPath.length() == 0) {
			System.out.println("Info: No data has been loaded");
			return;
		}
		
		// Create a data builder
		DataBuilder dataBuilder = new DataBuilder(EngineProperties.instance().getProperty(Property.DATA_PATH_VALUE));
			
		// Create a director and use the data builder to extract content
		Director director = new Director(dataBuilder);

		// Construct the content held by the director
		director.construct();
	}
}