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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import editor.application.Application;
import editor.menu.ProjectMenuItem;
import editor.menu.TileMapMenuItem;
import editor.models.TileMapModel;
import editor.models.TileModel;
import editor.views.ProjectView;
import framework.communication.external.builder.AbstractBuilder;
import framework.communication.external.filesystem.FileSystem;
import framework.core.factories.AbstractFactory;
import framework.core.factories.ModelFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.MenuBuilder;
import framework.core.system.GameValues;
import framework.utils.logging.Tracelog;

/**
 * Builder pattern used for exporting a map for games to use
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public final class ImportImageBuilder extends AbstractBuilder<FileSystem> {

    /**
     * Initialize the file chooser to the desktop of the user
     */
    private JFileChooser _fileChooser;

    /**
     * The tile map model from where the import will occur
     */
    private TileMapModel _tileMapModel;

    /**
     * Constructs a new instance of this class type
     */
    public ImportImageBuilder() {
        _fileChooser = new JFileChooser(System.getProperty("user.home") + File.separator + "desktop");
        _fileChooser.setAcceptAllFileFilterUsed(false);
        _fileChooser.setFileFilter(new FileNameExtensionFilter("Image File (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif"));
    }

    @Override public boolean buildStart() {

        // Set the directory location
        if(GameValues.exists(_fileChooser.getClass().getSimpleName())) {
            _fileChooser.setCurrentDirectory(new File(GameValues.getGameValue(_fileChooser.getClass().getSimpleName())));
        }
        
        // Ask the user where the file should be imported from
        if(_fileChooser.showOpenDialog(Application.instance()) != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        
        // Store within the game properties the selected directory
        GameValues.addGameProperty(_fileChooser.getClass().getSimpleName(), _fileChooser.getCurrentDirectory().toString());

        // If there is no main window, then there cannot be a project, therefore prompt the user
        // to create a new project, and very that all of that went through correctly
        if(AbstractFactory.getFactory(ViewFactory.class).get(ProjectView.class) == null) {
            MenuBuilder.search(Application.instance().getJMenuBar(), ProjectMenuItem.class).onExecute(null);
            if(AbstractFactory.getFactory(ViewFactory.class).get(ProjectView.class) == null) {
                return false;
            }
        }

        // Get the currently selected tile map, and if it is not found then prompt the creation for a new 
        // tile map and use that one when performing the import
        List<TileMapModel> tileMapModels = AbstractFactory.getFactory(ModelFactory.class).getAll(TileMapModel.class);
        Optional<TileMapModel> tileMap = tileMapModels.stream().filter(z -> z.getSelected()).findFirst();
        if(!tileMap.isPresent()) {				

            try {
                // Get the image that the user selected and prompt the user to create a new tile map with help 
                // from the dimensions of the selected image. Then check to see if there is a new tile map that was
                // created, and if there was then use that to perform the import.
                BufferedImage img = ImageIO.read(_fileChooser.getSelectedFile());

                // Get a reference to the new tile map creation, set its contents and execute the action, and then clear.
                TileMapMenuItem menuItem = MenuBuilder.search(Application.instance().getJMenuBar(), TileMapMenuItem.class);
                menuItem.setDimensions(new Dimension(img.getWidth(), img.getHeight()));
                menuItem.onExecute(null);
                menuItem.onReset();

                // Get the newly created tile map
                tileMap = AbstractFactory.getFactory(ModelFactory.class).getAll(TileMapModel.class).stream().filter(z -> !tileMapModels.contains(z)).findFirst();

            } catch (Exception exception) {
                Tracelog.log(Level.WARNING, true, exception);
            }
        }

        // If there is a tile map present then store it for later use within this builder
        if(!tileMap.isPresent()) {
            // Something bad occurred
            Tracelog.log(Level.WARNING, true, "Could not find the newly added tile map");

            // Something bad happened, do not proceed any further
            return false;
        }

        // Keep a reference onto the tile map for later use
        _tileMapModel = tileMap.get();

        // Create the file system with the selected import file
        _fileSystem = new FileSystem(_fileChooser.getSelectedFile());

        // Everything went well
        return true;
    }

    @Override public void buildContent() {

        // There must be a valid tile map model moving forward
        if(_tileMapModel == null) {
            Tracelog.log(Level.SEVERE, true, "Cannot build the content of the import image builder, tile map model invalid");
            return;
        }

        List<BufferedImage> images = _fileSystem.extractImages(_tileMapModel.getRows(), _tileMapModel.getColumns());
        List<TileModel> tiles = _tileMapModel.getTiles();

        if(tiles.size() != images.size() || tiles.size() == 0) {
            Tracelog.log(Level.SEVERE, true, "Tile size and/or image size mismatch detected, cannot continue with the importing into the current workspace");
            return;
        }

        for(int i = 0; i < images.size(); ++i) {
            tiles.get(i).setImage(images.get(i));
        }
    }

    @Override public void buildEnd() {
    }
}