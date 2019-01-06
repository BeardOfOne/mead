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

package editor.controllers;

import java.util.Optional;
import java.util.logging.Level;

import framework.communication.internal.signal.ISignalListener;
import framework.communication.internal.signal.ISignalReceiver;
import framework.communication.internal.signal.arguments.ControllerEventArgs;
import framework.communication.internal.signal.arguments.ModelEventArgs;
import framework.core.factories.AbstractFactory;
import framework.core.factories.ControllerFactory;
import framework.core.mvc.controller.BaseController;
import framework.utils.logging.Tracelog;

import editor.models.TileMapModel;
import editor.models.TileModel;

/**
 * This controller provides functionality that deals with a single tile map and the tiles that it occupies
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class TileMapController extends BaseController {

    /**
     * Signal name indicating that this tile map is to clear its currently selected tile
     */
    private final String EVENT_CLEAR_SELECTION = "EVENT_CLEAR_SELECTION";

    /**
     * The setup model associated to the tile map
     */
    private final TileMapModel _tileMapModel;

    /**
     * The currently selected tile 
     */
    private TileModel _currentlySelectedTile;

    /**
     * The currently selected tile map
     */
    private TileMapModel _currentlySelectedTileMap;

    /**
     * Constructs a new instance of this class type
     * 
     * @param setupModel The tile map setup parameters
     */
    public TileMapController(TileMapModel setupModel) {
        _tileMapModel = setupModel;
        setupModel.addListener(this);
        registerAllSignals();
    }

    /**
     * Adds the specified tile model to the list of tile models owned by this controller
     * 
     * Note: Adding the tile model to this controller will cause the model to change sizes
     *       to match the cell width and cell height of the setup associated to this controller
     * 
     * @param tileModel The tile model to add to this controller 
     */
    public void addTileEntity(TileModel tileModel) {

        // Normalized the specified tile model to the dimensions specified
        // by this tile map
        tileModel.setWidth(_tileMapModel.getWidth());
        tileModel.setHeight(_tileMapModel.getHeight());

        // Add the tile model to the list of tiles
        _tileMapModel.getTiles().add(tileModel);
    }

    /**
     * Gets the setup model associated to this controller
     * 
     * @return The setup model
     */
    public TileMapModel getSetupDetails() {
        return _tileMapModel;
    }

    /**
     * Toggles the selection state of this tile map
     * 
     * @return TRUE if the selection is now on, FALSE otherwise
     */
    public boolean toggleSelected() {
        _tileMapModel.setSelected(!_tileMapModel.getSelected());
        return _tileMapModel.getSelected();
    }

    /**
     * Toggles the selection state of a tile associated to this tile map
     * 
     * @param listener The listener of the entity to be selected
     */
    public void toggleSelectedTile(ISignalListener listener) {

        // Get the tile model associated to the specified listener
        TileModel tileModel = _tileMapModel.getTiles().stream().filter(z -> z.isModelListening(listener)).findFirst().get();

        // Get the value of the tile model found and switch its selection state 
        tileModel.setSelected(!tileModel.getSelected());
    }

    /**
     * Updates the properties window with the contents of this tile
     * 
     * @param listener The listener associated to where the event for updating took place
     */
    public void updateProperties(ISignalListener listener) {

        // Get the properties controller
        PropertiesController properties = AbstractFactory.getFactory(ControllerFactory.class).get(PropertiesController.class);

        // If the properties controller exists
        if(properties != null) {

            TileModel tileModel = null;
            if(listener instanceof TileModel) {
                tileModel = (TileModel) listener;
            }
            else {

                // Look for a tile that is selected
                Optional<TileModel> optionalTileModel = null;

                // Attempt to get the tile that was listened in on
                if(listener != null) {
                    optionalTileModel = _tileMapModel.getTiles().stream().filter(z -> z.isModelListening(listener)).findFirst();
                }

                // If there was no tile that could be found that was being listened in on, try and 
                // find the currently selected tile
                if(optionalTileModel == null || !optionalTileModel.isPresent()) {
                    optionalTileModel = _tileMapModel.getTiles().stream().filter(z -> z.getSelected()).findFirst();	    		   
                }

                // Get the tile model that was found (if any)
                if(optionalTileModel.isPresent()) {
                    tileModel = optionalTileModel.get();
                }
            }

            if(properties.getSelectedTile() == tileModel) {
                
            }
            else if(!properties.isTileSelected()) {
                properties.loadContents(tileModel);   
            }
        }
    }

    /**
     * Sets the location of this tile map
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public void updateTileMapPosition(int x, int y) {
        _tileMapModel.setCoordinate(x, y);
    }

    private void registerAllSignals() {

        addSignal(EVENT_CLEAR_SELECTION, new ISignalReceiver<ControllerEventArgs>() {
            @Override public void signalReceived(ControllerEventArgs event) {

                if(_currentlySelectedTile != null && _currentlySelectedTile.getSelected()) {
                    _currentlySelectedTile.setSelected(false);
                }

                if(_currentlySelectedTileMap != null && _currentlySelectedTileMap.getSelected()) {
                    _currentlySelectedTileMap.setSelected(false);
                }

                _currentlySelectedTile = null;
                _currentlySelectedTileMap = null;
            }
        });

        addSignal(TileModel.EVENT_TILE_SELECTION_CHANGED, new ISignalReceiver<ModelEventArgs>() {
            @Override public void signalReceived(ModelEventArgs event) {

                // Unregister this controller from listening to this event, to prevent  an event from being re-fired back here
                setSignalEnabled(event.getOperationName(), false);

                TileModel tileModel = (TileModel) event.getSource();
                if(tileModel.getSelected()) {

                    // If there is a tile held by this controller then remove it's selection.  This occurs
                    // when a tile within the same tile map is selected, when there was already a tile selected
                    // within the same tile map prior
                    if(_currentlySelectedTile != null && _currentlySelectedTile != tileModel) {
                        _currentlySelectedTile.setSelected(false);
                    }

                    // Assign over the new selection
                    _currentlySelectedTile = tileModel;

                    // Inform all the tile map controllers to clear their selection (if any)
                    AbstractFactory.getFactory(ControllerFactory.class).multicastSignalListeners(
                            TileMapController.class, 
                            new ControllerEventArgs(TileMapController.this, EVENT_CLEAR_SELECTION)
                            );

                    // If this tile map is already selected, then remove it's selection to allow the tile
                    // to be selected.
                    //
                    // Note: If a tile map is selected, then selecting on a tile should remove the tile map selection
                    if(_currentlySelectedTileMap != null && _currentlySelectedTileMap.getSelected()) {
                        _currentlySelectedTileMap.setSelected(false);
                        _currentlySelectedTileMap = null;
                    }

                    // Update the properties window
                    updateProperties(_currentlySelectedTile);
                }
                // If we receive the same tile, then at this point it is already unselected so just clear the reference
                else if(tileModel.equals(_currentlySelectedTile)){

                    // Update the properties window
                    updateProperties(_currentlySelectedTile);

                    _currentlySelectedTile = null;
                }
                else {
                    Tracelog.log(Level.SEVERE, true, "Something bad happened...we need to handle the tile selection cases better");
                }

                // Register back the original listener so that we can continue to receive signals
                setSignalEnabled(event.getOperationName(), true);
            }
        });

        addSignal(TileMapModel.EVENT_TILEMAP_SELECTION_CHANGED, new ISignalReceiver<ModelEventArgs>() {
            @Override public void signalReceived(ModelEventArgs event) {

                // Unregister this controller from listening to this event, to prevent 
                // an event from being re-fired back here
                setSignalEnabled(event.getOperationName(), false);

                TileMapModel tileMapModel = (TileMapModel) event.getSource();
                if(tileMapModel.getSelected()) {

                    // Assign over the new selection
                    _currentlySelectedTileMap = tileMapModel;

                    // Inform all the tile map controllers to clear their selection (if any)
                    AbstractFactory.getFactory(ControllerFactory.class).multicastSignalListeners(
                            TileMapController.class, 
                            new ControllerEventArgs(TileMapController.this, EVENT_CLEAR_SELECTION)
                            );

                    // If a tile within this tile map is selected, remove it's selection to allow the tile map selection to occur
                    //
                    // Note: If a tile is selected, then selecting on a tile map should remove the tile selection
                    if(_currentlySelectedTile != null && _currentlySelectedTile.getSelected()) {
                        _currentlySelectedTile.setSelected(false);
                        _currentlySelectedTile = null;
                    }
                }
                // If we receive the same tile, then at this point it is already unselected so just clear the reference
                else if(tileMapModel.equals(_currentlySelectedTileMap)){
                    _currentlySelectedTileMap = null;
                }
                else {
                    Tracelog.log(Level.SEVERE, true, "Something bad happened...we need to handle the tile selection cases better");
                }

                // Register back the original listener so that we can continue to receive signals
                setSignalEnabled(event.getOperationName(), true);
            }
        });
    }
}