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

package editor.controllers;

import javax.swing.DefaultComboBoxModel;

import editor.models.TileLayerModel;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ModelFactory;
import framework.core.mvc.controller.BaseController;

/**
 * The controller for business logic associated to tile layers
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class TileLayersController extends BaseController {

    /**
     * The list of tile layer models
     */
    private final DefaultComboBoxModel<TileLayerModel> _layers = new DefaultComboBoxModel();

    /**
     * Constructs a new instance of this class type
     */
    public TileLayersController() {


        int size = AbstractSignalFactory.getFactory(ModelFactory.class).getQueuedResourcesCount(TileLayerModel.class);
        for(int i = 0; i < size; ++i) {
            _layers.addElement(AbstractSignalFactory.getFactory(ModelFactory.class).add(new TileLayerModel(), false));
        }
    }

    public DefaultComboBoxModel<TileLayerModel> getLayersModel() {
        return _layers;
    }

    /**
     * Adds a new layer entry
     * 
     * @param text The name of the layer
     * 
     * @return TRUE if the layer was created, FALSE otherwise
     */
    public boolean addLayer(String text) {

        // If the text is invalid then go no further
        if(text == null || (text = text.trim()).isEmpty()) {
            return false;
        }

        // Look for another layer with the same name, and if one is found then
        // go no further
        for(int i = 0, size = _layers.getSize(); i < size; ++i) {
            TileLayerModel layer = _layers.getElementAt(i);
            if(layer.getName().equalsIgnoreCase(text)) {
                return false;
            }
        }

        // Create the new layer and add it to our list
        TileLayerModel layer = AbstractSignalFactory.getFactory(ModelFactory.class).add(new TileLayerModel(), false);
        layer.setLayerName(text);
        _layers.addElement(layer);

        return true;
    }

    /**
     * Removes the specified tile layer model from the list of available tile layers
     * 
     * @param layer The tile layer model to remove
     * 
     * @return TRUE if the layer was created, FALSE otherwise
     */
    public boolean removeLayer(TileLayerModel layer) {

        if(layer == null) {
            return false;
        }

        // Go through all the layers and determine the one we wish to delete
        // Note: Calling .RemoveElement from the list does not work, potentially a bug on their end 
        for(int i = 0, size = _layers.getSize(); i < size; ++i) {
            if(_layers.getElementAt(i).equals(layer)) {
                _layers.removeElementAt(i);
                return true;
            }
        }

        return false;
    }

    /**
     * Updates the specified tile layer model from the list of available tile layers
     *
     * @param layer The tile layer model to update
     * @param name The name to set the layer to
     * 
     * @return TRUE if the layer was updated, FALSE otherwise
     */
    public boolean updateLayer(TileLayerModel layer, String name) {
        if(layer == null) {
            return false;
        }
        
        for(int i = 0, size = _layers.getSize(); i < size; ++i) {
            if(_layers.getElementAt(i).getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        
        layer.setLayerName(name);
        return true;
    }
}