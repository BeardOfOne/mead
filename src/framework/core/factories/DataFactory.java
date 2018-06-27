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

package framework.core.factories;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;

import framework.api.IData;
import framework.core.system.EngineProperties;
import framework.core.system.EngineProperties.Property;
import framework.utils.logging.Tracelog;

/**
 * Data factory for getting data related resources
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class DataFactory extends AbstractFactory {

    /**
     * A data element structure that contains that contents of data and the associated buffered image
     * 
     * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
     *
     */
    private class DataElement {
        public final Image image;
        public final IData data;
        
        public DataElement(IData data, Image image) {
            this.image = image;
            this.data = data;
        }
    }
    
    /**
     * A mapping of identifiers to associated data image
     */
    private final Map<UUID, List<DataElement>> _data = new HashMap();
    
    /**
     * Gets the data entity using the specified identifier
     *
     * @param identifier The identifier to use as a lookup
     * 
     * @return A data entity
     */
    public Image getDataEntity(UUID identifier) {
        
        Image img = null;
        for(Entry<UUID, List<DataElement>> datas : _data.entrySet()) {
            Optional<DataElement> dataEntity = datas.getValue().stream().filter(z -> z.data.getIdentifier().equals(identifier)).findFirst();
            if(dataEntity.isPresent()) {
                img = dataEntity.get().image;
                break;
            }
        }
        
        return img; 
    }

    /**
     * Adds the specified data resources in this factory.  The current structure of the data resources
     * consists of a mapping of layer names to {@link IData} implemented concrete types
     * 
     * @param resources The list of resources
     */
    public void populateData(Collection<IData> resources)  {

        // Get the list of layer UUID, make sure there are no duplicates
        Set<UUID> layers = resources.stream().map(IData::getLayers).flatMap(Collection::stream).collect(Collectors.toSet());
       
        BufferedImage mainImage = null;
        try {
            mainImage = ImageIO.read(getClass().getResourceAsStream(EngineProperties.instance().getProperty(Property.DATA_PATH_SHEET)));    
        }
        catch(Exception exception) {
            Tracelog.log(Level.SEVERE, false, exception);
        }

        // Go through all the layers and look for the data that contains the specified UUID
        for(UUID uuid : layers) {
                    
            List<DataElement> dataElements = new ArrayList();
            for(IData dataElement : resources.parallelStream().filter(z -> z.getLayers().contains(uuid)).collect(Collectors.toList())) {
                dataElements.add(new DataElement(dataElement, getImageData(mainImage, dataElement)));
            }
            
            _data.put(uuid, dataElements);
        }
        
        // Cleanup the mainImage contents
        if(mainImage != null) {
            mainImage.flush();
        }
    }
    
    private Image getImageData(BufferedImage image, IData data) {
        
        // Attempt to get the image portion
        try {
            Point tl = data.getTopLeft();
            Point br = data.getBottomRight();
            return image.getSubimage(tl.x, tl.y, br.x - tl.x, br.y - tl.y);
        }
        catch(Exception exception) {
            Tracelog.log(Level.SEVERE, false, exception);
        }

        return null;
    }

    @Override protected boolean hasEntities() {
        return _data.values().size() > 0;
    }
    
    @Override protected boolean isPersistent() {
        return true;
    }
}