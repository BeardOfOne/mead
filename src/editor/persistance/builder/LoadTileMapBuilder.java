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

package editor.persistance.builder;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.xml.bind.JAXBException;

import editor.application.Application;
import editor.models.ProjectModel;
import editor.models.TileLayerModel;
import editor.models.TileMapModel;
import editor.models.TileModel;
import editor.views.LayersDialogView;
import editor.views.ProjectView;
import editor.views.TileMapView;
import framework.communication.external.builder.AbstractBuilder;
import framework.communication.external.filesystem.FileSystem;
import framework.communication.internal.persistance.IXMLCodec.XMLCodec;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ModelFactory;
import framework.core.factories.ViewFactory;
import framework.core.system.GameValues;
import framework.utils.logging.Tracelog;

/**
 * Builder pattern used for loading a map
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public final class LoadTileMapBuilder extends AbstractBuilder<FileSystem> {

    /**
     * Builds the list of layers back into the application
     */
    private void buildLayers() {

        // Get the list of layer models from the file system
        List<TileLayerModel> tileLayerModels = _fileSystem.getData(TileLayerModel.class);

        if(tileLayerModels != null) {
            for(TileLayerModel tileLayerModel  : tileLayerModels) {
                // Queue the resources from the file system for later consumption 
                AbstractSignalFactory.getFactory(ModelFactory.class).queueResource(tileLayerModel);
            }

            // Load up the tile layers view.  What this does is it forces the view to be created, thus 
            // generating a controller, which will take the above layers that have been pushed on the queue
            // and will populate the necessary structures for the layers to exist within the application.
            AbstractSignalFactory.getFactory(ViewFactory.class).add(new LayersDialogView(), true);
        }
    }

    /**
     * Builds the main project surrounding the application session
     */
    private void buildProject() {

        List<ProjectModel> projectModels = _fileSystem.getData(ProjectModel.class);
        if(projectModels.size() != 1) {
            Tracelog.log(Level.SEVERE, true, "Could not load the main project, something went wrong");
            return;
        }

        // Get a reference to the project model
        ProjectModel model = projectModels.get(0);

        // Get the list of tile map setup models from the file system and queue them into the factory
        AbstractSignalFactory.getFactory(ModelFactory.class).queueResource(model);

        // Create a new main window view that is shared among the entire application and render it
        AbstractSignalFactory.getFactory(ViewFactory.class).add(new ProjectView(model.getName()), true);

    }

    /**
     * Builds the list of tile maps back into the application
     */
    private void buildTileMaps() {
        // Get the list of tile maps from the file system
        List<TileMapModel> tileMaps = _fileSystem.getData(TileMapModel.class);

        // Go through each tile map and queue it in the factory
        for(TileMapModel model : tileMaps) {

            // Get the list of tile map setup models from the file system and queue them into the factory
            AbstractSignalFactory.getFactory(ModelFactory.class).queueResource(model);

            // Get the list of tile map setup models from the file system and queue them into the factory
            for(TileModel tileMapModel : model.getTiles()) {
                AbstractSignalFactory.getFactory(ModelFactory.class).queueResource(tileMapModel);	
            }

            // Clear the list of tiles, they should only exist in the cache
            model.getTiles().clear();

            // Create a new tilemap view and render it's contents
            TileMapView tileMapView = AbstractSignalFactory.getFactory(ViewFactory.class).add(
                    new TileMapView(model.getName(), model.getRows(), model.getColumns(), model.getWidth(), model.getHeight()), 
                    false
                    );
            
            tileMapView.setLocation(
                model.getXCoordinate(),
                model.getYCoordinate()
            );

            tileMapView.render();
        }
    }

    @Override public void buildContent() {

        // Build the project contents
        buildProject();

        // Layers must be built before the tile maps or the layers will be cleared
        buildLayers();

        // Build the list of tile maps
        buildTileMaps();
    }

    @Override public void buildEnd() {
        AbstractSignalFactory.getFactory(ViewFactory.class).get(ProjectView.class).render();
    }

    @Override public boolean buildStart() {

        // Create a file chooser so that we can get information about
        // the save process
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + File.separator + "desktop");

        // Set the directory location
        if(GameValues.exists(fileChooser.getClass().getSimpleName())) {
            fileChooser.setCurrentDirectory(new File(GameValues.getGameValue(fileChooser.getClass().getSimpleName())));
        }

        // Open the file chooser so the user can navigate to the path
        // where the file should be loaded.
        int loadValue = fileChooser.showOpenDialog(Application.instance());

        // If the user chose to perform a load within the file chooser
        // dialog, then continue forward 
        if(loadValue == JFileChooser.APPROVE_OPTION) {

            // Store within the game properties the selected directory
            GameValues.addGameProperty(fileChooser.getClass().getSimpleName(), fileChooser.getCurrentDirectory().toString());

            // Create the codec for the file system
            XMLCodec codec = XMLCodec.createInstance(FileSystem.class);
            
            try {
                // Get the selected map file and unmarshal it
                Object fileSystem = codec.getUnmarshaller().unmarshal(fileChooser.getSelectedFile());

                // Create the file system 
                _fileSystem = (FileSystem) fileSystem;

                // Return true to indicate everything went well
                return true;
            } 
            catch (JAXBException exception) {
                Tracelog.log(Level.SEVERE, true, exception);
            }
        }

        return false;
    }
}