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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import framework.api.IModel;
import framework.communication.internal.signal.ISignalListener;
import framework.communication.internal.signal.ISignalReceiver;
import framework.communication.internal.signal.arguments.ModelEventArgs;
import framework.core.mvc.model.BaseModel;


/**
 * Model representation of a single tile within the tilemap structure
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="tile")
public final class TileModel extends BaseModel {

    /**
     * Signal name indicating that this model's selection state has changed
     */
    public static final String EVENT_TILE_SELECTION_CHANGED = "EVENT_TILE_SELECTION_CHANGED";

    /**
     * Signal name indicating that this model's image has changed
     */
    public static final String EVENT_IMAGE_CHANGED = "EVENT_IMAGE_CHANGED";

    /**
     * The width of the tile
     */
    @XmlAttribute(name="width")
    private int _width;

    /**
     * The height of the tile
     */
    @XmlAttribute(name="height")
    private int _height;

    /**
     * The name of this tile
     */
    @XmlAttribute(name="name")
    private String _name = "";

    /**
     * The friendly name of this tile
     */
    @XmlAttribute(name="fname")
    private String _friendlyName = "";

    /**
     * The layers that this tile is associated with
     */
    @XmlElement(name="layer")
    private List<UUID> _layers = new ArrayList();

    /**
     * The image of this tile
     */
    @XmlAttribute(name="image")
    private Image _image;

    /**
     * The selection state of this tile
     */
    private transient boolean _selectionState;

    /**
     * Constructs a new instance of this class type
     *
     * Note: Explicit default constructor
     */
    public TileModel() {	
    }

    /**
     * Constructs a new instance of this class type
     *
     * @param receiver The list of receivers that will receive events when this model changes
     * @param <T> A type that extends from ISignalListener
     */
    public <T extends ISignalListener> TileModel(T... receiver) {
        super(receiver);
    }

    /**
     * Constructs a new instance of this class type
     *
     * @param width The width
     * @param height The height
     */
    public TileModel(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * Sets the selection state of this model
     * 
     * @param selectionState The selection state of this model
     */
    public void setSelected(boolean selectionState) {
        _selectionState = selectionState;
        setOperation(EVENT_TILE_SELECTION_CHANGED);
        doneUpdating();
    }

    /**
     * Gets the selection state of this tile
     * 
     * @return If this tile is selected
     */
    public boolean getSelected() {
        return _selectionState;
    }

    /**
     * Sets the buffered image for this model
     * 
     * @param image the image for this model
     */
    public void setImage(BufferedImage image) {

        if(image == null) {
            _image = image;
        }
        else {
            // Scaling an image turns the image into a sun.awt type image, we need to turn it
            // back into a buffered image
            Image scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);

            // Create a buffer with the same width and height with the specified color model
            // and draw the contents of the scaled image into the buffer
            BufferedImage buff = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            buff.getGraphics().drawImage(scaledImage, 0, 0, null);

            _image = buff;
        }

        setOperation(EVENT_IMAGE_CHANGED);
        doneUpdating();
    }

    /**
     * Gets the image associated to the tile model
     * 
     * @return The image of the tile model
     */
    public Image getImage() {
        return _image;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return _width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this._width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return _height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this._height = height;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @return the friendlyName
     */
    public String getFriendlyName() {
        return _friendlyName;
    }

    /**
     * @param friendlyName the friendlyName to set
     */
    public void setFriendlyName(String friendlyName) {
        this._friendlyName = friendlyName;
    }

    /**
     * @return the layers
     */
    public List<UUID> getLayers() {
        return new ArrayList(_layers);
    }

    /**
     * Updates the layers associated to this tile 
     *  
     * @param layerUUID The list of layer UUIDs
     */
    public void updateLayers(List<UUID> layerUUID) {
        _layers.clear();
        _layers.addAll(new ArrayList(layerUUID));
    }
    
    public void clearData() {
        setSuppressUpdates(true);
        setFriendlyName("");
        setName("");
        setImage(null);
        updateLayers(new ArrayList<UUID>());
        setSuppressUpdates(false);
        doneUpdating();
    }
    
    /**
     * @return the name
     */
    @Override public String getName() {
        return _name;
    }

    @Override public void copyData(IModel model) {
        super.copyData(model);
        if(model instanceof TileModel) {
            TileModel tileModel = (TileModel) model;
            setName(tileModel.getName());
            setFriendlyName(tileModel.getFriendlyName());
            setWidth(tileModel.getWidth());
            setHeight(tileModel.getHeight());
            updateLayers(tileModel._layers);
            setImage((BufferedImage)tileModel._image);
        }
    }

    @Override public void registerSignalListeners() {
        super.registerSignalListeners();

        // Listen to the remove layer event
        addSignalListener(TileLayerModel.EVENT_REMOVE_LAYER, new ISignalReceiver<ModelEventArgs>() {
            @Override public void signalReceived(ModelEventArgs event) {
                if(event.getSource() instanceof TileLayerModel) {
                    TileLayerModel tileLayerModel = (TileLayerModel) event.getSource();
                    _layers.remove(tileLayerModel.getUUID());
                }
            }
        });
    }

    @Override public String toString() {
        String name = getFriendlyName();
        if(name == null || name.length() == 0) {
            return super.toString();	
        }

        return String.format("%s: %s", getClass().getName(), name);
    }
}