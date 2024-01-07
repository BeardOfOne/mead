package editor.models;

import framework.communication.internal.signal.ISignalListener;
import framework.communication.internal.signal.arguments.EventArgs;
import framework.core.mvc.model.BaseModel;

/**
 * The model associated to the properties of a particular tile 
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class PropertiesModel extends BaseModel {

    /**
     * The tile model currently being used in the properties pipeline
     */
    private TileModel _tileModel;

    /**
     * Constructs a new instance of this class type
     * 
     * @param receivers The list of receivers to receive event notifications
     */
    public PropertiesModel(ISignalListener... receivers) {
        super(receivers);
    }

    /**
     * Sets the specified tile model
     * 
     * @param model The tile model
     */
    public void setTileModel(TileModel model) {
        _tileModel = model;
        doneUpdating();
    }

    /**
     * Gets the tile model associated to this model
     * 
     * @return The tile model
     */
    public TileModel getTileModel() {
        return _tileModel;
    }

    @Override public void update(EventArgs signalEvent) {
    }
}