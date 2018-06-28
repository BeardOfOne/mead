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
import java.util.Map;

import javax.swing.JFileChooser;

import editor.application.Application;
import editor.models.ProjectModel;
import editor.models.TileLayerModel;
import editor.models.TileMapModel;
import framework.api.IModel;
import framework.communication.external.builder.AbstractBuilder;
import framework.communication.external.filesystem.FileSystem;
import framework.communication.internal.persistance.IXMLCodec;
import framework.communication.internal.signal.IDataPipeline;
import framework.communication.internal.signal.SignalListenerContainer;
import framework.communication.internal.signal.arguments.AbstractEventArgs;
import framework.communication.internal.signal.arguments.PipelinedEventArgs;
import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ModelFactory;
import framework.core.system.GameValues;

/**
 * Builder pattern used for saving a map, so that it can be loaded back afterwards
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public final class SaveTileMapBuilder extends AbstractBuilder<FileSystem> implements IDataPipeline<IModel> {

    @Override public boolean buildStart() {

        // Create a file chooser so that we can get information about
        // the save process
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + File.separator + "desktop");
        fileChooser.setSelectedFile(new File(""));

        // Set the directory location
        if(GameValues.exists(fileChooser.getClass().getSimpleName())) {
            fileChooser.setCurrentDirectory(new File(GameValues.getGameValue(fileChooser.getClass().getSimpleName())));
        }

        // Open the file chooser so the user can navigate to the path
        // where the file should be saved.  The user will also enter a
        // file name, this is taken and will later be used to create
        // the file
        int saveValue = fileChooser.showSaveDialog(Application.instance());

        // If the user chose to perform a save within the file chooser
        // dialog, then continue forward and create a resource handle
        // to the specified file
        if(saveValue == JFileChooser.APPROVE_OPTION) {

            // Store within the game properties the selected directory
            GameValues.addGameProperty(fileChooser.getClass().getSimpleName(), fileChooser.getCurrentDirectory().toString());

            // Create a file system at the specified path
            _fileSystem = new FileSystem(
                    new File(fileChooser.getSelectedFile().getAbsolutePath() + IXMLCodec.EXTENSION_XML)
                    );

            return true;
        }

        return false;
    }

    @Override public void buildContent() {

        // Store a reference to the project model.  There should only ever be one of these within
        // the entire application
        _fileSystem.write(
                AbstractFactory.getFactory(ModelFactory.class).get(ProjectModel.class)
                );

        // Send a message out to the setup model indicating that its contents should
        // be piped back to us
        AbstractSignalFactory.getFactory(ModelFactory.class).multicastSignalListeners(
                TileMapModel.class, 
                new PipelinedEventArgs(this, IModel.EVENT_PIPE_DATA)
                );

        // Send out a signal to the layers to indicate that there
        // be a serialize 
        AbstractSignalFactory.getFactory(ModelFactory.class).multicastSignalListeners(
                TileLayerModel.class, 
                new PipelinedEventArgs(this, IModel.EVENT_PIPE_DATA)
                );
    }

    @Override public void pipeData(IModel data) {
        _fileSystem.write(data);
    }

    @Override public void buildEnd() {
        _fileSystem.serialize();
    }


    @Override public Map<String, SignalListenerContainer> getSignalListeners() {
        return null;
    }

    @Override public void update(AbstractEventArgs signalEvent) {
    }
}