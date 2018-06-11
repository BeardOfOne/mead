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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import engine.api.IData;
import engine.core.factories.AbstractFactory;
import engine.utils.logging.Tracelog;

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
        if(data == null) {
            data = new ArrayList();
        }

        return new ArrayList(data);
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
     * Adds the specified data resources in this factory.  The current structure of the data resources
     * consists of a mapping of layer names to {@link IData} implemented concrete types
     * 
     * @param resources The list of resources
     */
    public void populateData(List<IData> resources)  {
        
        // Clear any data within this factory first
        _data.clear();
        
        // Get the list of layer UUID, make sure there are no duplicates
        Set<UUID> layers = resources.stream().map(IData::getLayers).flatMap(Collection::stream).collect(Collectors.toSet());
        
        // Go through all the layers and look for the data that contains the specified UUID
        for(UUID uuid : layers) {
            
            // Get all the data that has the specified layer
            List<IData> data = new ArrayList(
                resources.stream().filter(z -> z.getLayers().contains(uuid)).collect(Collectors.toList())
            );
            
            _data.put(uuid, data);
        }        
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