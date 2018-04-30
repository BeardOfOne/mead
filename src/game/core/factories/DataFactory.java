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

package game.core.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import engine.api.IData;
import engine.communication.external.builder.Director;
import engine.core.factories.AbstractFactory;
import engine.core.system.EngineProperties;
import engine.core.system.EngineProperties.Property;
import engine.utils.logging.Tracelog;
import game.data.DataBuilder;

/**
 * Data factory for getting data related resources
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class DataFactory extends AbstractFactory {

    /**
     * A mapping of identifiers to associated datas
     */
    private final Map<UUID, List<IData>> _data = new HashMap();

    /**
     * Gets a data group using the specified identifer
     *
     * @param identifier The identifier to use as a lookup
     * 
     * @return A data group
     */
    public List<IData> getDataGroup(UUID identifier) {
        List<IData> data = _data.get(identifier);
        if(data != null) {
            data = new ArrayList(data); 
        }
        return data;
    }
    
    /**
     * Gets the data entity using the specified identifier
     *
     * @param identifier The identifier to use as a lookup
     * 
     * @return A data entity
     */
    public IData getDataEntity(UUID identifier) {
        for(Entry<UUID, List<IData>> datas : _data.entrySet()) {
            Optional<IData> dataEntity = datas.getValue().stream().filter(z -> z.getIdentifier().equals(identifier)).findFirst();
            if(dataEntity.isPresent()) {
                return dataEntity.get();
            }
        }
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
     */
    public void populateData(List<IData> resources)  {
        
        // TODO
        
//        // Create a mapping of layers to associated data
//        Map<List<UUID>, List<IData>> mappings = resources.stream().collect(Collectors.groupingBy(IData::getIdentifier()));
//
//        // Go through each kvp and add its contents into the factory
//        for(Map.Entry<List<String>, List<IData>> mapping : mappings.entrySet()) {
//
//            // Go through the list of layers, since there can be more than one layer per entity
//            for(String layer : mapping.getKey()) {
//
//                // If the entry does not exist then create a new list
//                // and insert it into the map
//                List<IData> dataList = _data.get(layer);
//                if(dataList == null) {
//                    dataList = new ArrayList();
//                    _data.put(layer, dataList);
//                }
//
//                // Add the data entry into the mappings structure
//                dataList.addAll(mapping.getValue());
//            }
//        }
    }

    @Override protected boolean hasEntities() {
        return _data.values().size() > 0;
    }
    
    @Override protected boolean isPersistent() {
        return true;
    }

    @Override public void clear() {
        Tracelog.log(Level.INFO, false, "clear() for DataFactory.java not implemented");
    }

    @Override public void destructor() {
        Tracelog.log(Level.INFO, false, "remove() for DataFactory.java not implemented");
    }
}