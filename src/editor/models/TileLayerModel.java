/**
 * Daniel Ricci <thedanny09@icloud.com>
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

package editor.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import framework.api.IModel;
import framework.communication.internal.signal.arguments.EventArgs;
import framework.core.mvc.model.BaseModel;

/**
 * This model represents a tile layer.  A tile layer represents a logical grouping of one or more tiles
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="layer")
public final class TileLayerModel extends BaseModel {

    public static final String EVENT_REMOVE_LAYER = "EVENT_REMOVE_LAYER";

    /**
     * The name of the layer
     */
    @XmlAttribute(name="name")
    private String _name;

    /**
     * Constructs a new instance of this class type
     * 
     * Note: Explicit default constructor
     *
     */
    public TileLayerModel() {
    }

    /**
     * Sets the name of this layer
     * 
     * @param layerName The name to set for this layer
     */
    public void setLayerName(String layerName) {
        this._name = layerName;
    }

    @Override public String getName() {
        return _name;
    }

    @Override public void copyData(IModel model) {
        super.copyData(model);

        if(model instanceof TileLayerModel) {
            TileLayerModel layer = (TileLayerModel) model;
            setLayerName(layer._name);
        }
    }

    @Override public String toString() {
        return _name;
    }

    @Override public void update(EventArgs signalEvent) {
    }
}