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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import engine.api.IData;
import engine.communication.external.builder.Director;
import engine.core.system.EngineProperties;
import engine.core.system.EngineProperties.Property;
import engine.utils.logging.Tracelog;
import game.data.DataBuilder;

/**
 * Data factory for extracting data from an external source
 * based on specified types
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 * @param <T> An {@link IData} implemented type
 */
public abstract class AbstractDataFactory<T extends IData> extends AbstractFactory {

	/**
	 * Holds the mappings of layer names to data resources.
	 * Key = The name of the layer
	 * Value = All the IData types that have that layer name
	 * 
	 * Note: The key should be lowercase at all times for normalization reasons
	 */
	private final Map<String, List<T>> _data = new HashMap();
	
	/**
	 * Constructs a new instance of this class type
	 */
	public AbstractDataFactory() {
		isPersistent = true;
	}
	
	/**
	 * Gets a data resource based on the specified layer name and the data name
	 * 
	 * @param layerName The name of the layer to perform the lookup on
	 * @param dataName The name of the resource to lookup with the specified layer
	 * 
	 * @return A data resource of type {@link IData}
	 */
	public T getByName(String layerName, String dataName) {
		
		// Get the list of resource associated to the class type
		List<T> resources = getByLayer(layerName);
		
		// If the resources exist
		if(resources != null) {
			
			// Go through the list of resources and try to find the
			// resource with the specified name
			for(T resource : resources) {
				if(resource.getName().equalsIgnoreCase(dataName.toLowerCase())) {
					return resource;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the list of resources associated to the specified layer name
	 * 
	 * @param layerName The name of the layer to lookup
	 * 
	 * @return The list of {@link IData} types associated to the specified layer name
	 */
	public List<T> getByLayer(String layerName) {

		// Get the list of data
		List<T> data = _data.get(layerName.toLowerCase());
		
		// If there is a valid entry then return a new list 
		// of its results
		if(data != null) {
			return new ArrayList(data); 
		}

		// Nothing found
		return null;
	}
	
	/**
	 * Loads the data specified by the engine data path
	 */
	public void loadData() {
		
		// Get the value set within the engine properties for where to fetch 
		// the data from
		String dataPath = EngineProperties.instance().getProperty(Property.DATA_PATH_XML);
		
		// If the data path has not been set or it was set inappropriately
		if(dataPath == null || dataPath.length() == 0) {
			Tracelog.log(Level.WARNING, false, "No data path has been specified for the DATA_PATH_XML, data will not be loaded for the application");
			return;
		}
		
		// Create a data builder
		DataBuilder dataBuilder = new DataBuilder(dataPath);
			
		// Create a director and use the data builder to extract content
		Director director = new Director(dataBuilder);

		// Construct the content held by the director
		director.construct();
	}
	
	/**
	 * Adds the specified data resources in this factory.  The current structure of the data resources
	 * consists of a mapping of layer names to {@link IData} implemented concrete types
	 * 
	 * @param resources The list of resources
	 * @param <U> IData type
	 */
	public <U extends T> void addDataResources(List<U> resources)  {
		
		// Create a mapping of layer name to IData types
		Map<List<String>, List<U>> mappings = resources.stream().collect(Collectors.groupingBy(U::getLayerNames));
		
		// Go through each kvp and add its contents into the factory
		for(Map.Entry<List<String>, List<U>> mapping : mappings.entrySet()) {
			
			// Go through the list of layers, since there can be more than one layer
			// per entity
			for(String layer : mapping.getKey()) {

				// If the entry does not exist then create a new list
				// and insert it into the map
				List<T> dataList = _data.get(layer.toLowerCase());
				if(dataList == null) {
					dataList = new ArrayList();
					_data.put(layer.toLowerCase(), dataList);
				}

				// Add the data entry into the mappings structure
				dataList.addAll(mapping.getValue());
			}
		}
	}
	
	@Override protected boolean hasEntities() {
		return !_data.isEmpty();
	}
}