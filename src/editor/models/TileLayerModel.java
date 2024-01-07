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