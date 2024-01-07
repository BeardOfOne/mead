package editor.controllers;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

import framework.core.mvc.controller.BaseController;

import editor.models.PropertiesModel;
import editor.models.TileLayerModel;
import editor.models.TileModel;

/**
 * The properties controller
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class PropertiesController extends BaseController {

    /**
     * The properties model
     */
    private final PropertiesModel _model;

    /**
     * Constructs a new instance of this class type
     * 
     * @param model The properties model
     */
    public PropertiesController(PropertiesModel model) {
        _model = model;
    }

    /**
     * Checks if the tile associated to this controller is selected
     * 
     * @return TRUE if the tile is selected, FALSE if the tile is not selected
     */
    public boolean isTileSelected() {
        return _model != null && _model.getTileModel() != null && _model.getTileModel().getSelected();
    }

    /**
     * Gets the tile map model associated to the properties window
     * 
     * @return The tile map model 
     */
    public TileModel getSelectedTile() {
        return _model.getTileModel();
    }

    /**
     * Sets the image buffer of the tile associated to this controller
     * 
     * @param buffer The image buffer
     */
    public void setData(BufferedImage buffer) {
        if(_model != null && _model.getTileModel() != null) {
            _model.getTileModel().setImage(buffer);
        }
    }

    /**
     * Sets the layer of the tile associated to this controller
     * 
     * @param layers The list of layer models to set for the currently active tile
     * 
     * Note: An empty list is acceptable when resetting the layers of the currently active tile
     */
    public void setLayers(List<TileLayerModel> layers) {
        if(_model != null && _model.getTileModel() != null && layers != null) {
            _model.getTileModel().updateLayers(layers.stream().map(z -> z.getUUID()).collect(Collectors.toList()));	
        }
    }

    /**
     * Gets the name property of the tile model associated to this property functionality
     * 
     * @return The name of the tile model
     */
    public String getName() {
        return _model.getTileModel().getName();
    }

    /**
     * Sets the name of the tile associated to this controller
     * 
     * @param name The name to set the tile to
     */
    public void setName(String name) {
        _model.getTileModel().setName(name);
    }

    /**
     * Sets the friendly name
     * 
     * @param friendlyName The friendly name text
     */
    public void setFriendlyName(String friendlyName) {

        // Get the current tile model
        TileModel currentTileModel = _model.getTileModel();

        // Set the friendly name of the tile model
        currentTileModel.setFriendlyName(friendlyName.trim());
    }
    
    public void resetData() {
        _model.getTileModel().clearData();
    }

    /**
     * Loads the contents of the specified tile model into the properties window
     * 
     * Note: This should at all costs only display the model in a read-only fashion
     * Note: Pass in null as a parameter of the tile model to clear the contents
     *  
     * @param tileModel The tile to display
     */
    public void loadContents(TileModel tileModel) {
        this._model.setTileModel(tileModel);
    }


    @Override public void clearSignals() {
        _model.setTileModel(null);
    }
}