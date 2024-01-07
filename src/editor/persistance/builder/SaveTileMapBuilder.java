package editor.persistance.builder;

import java.io.File;
import java.util.Map;

import javax.swing.JFileChooser;

import framework.api.IModel;
import framework.communication.external.builder.AbstractBuilder;
import framework.communication.external.filesystem.FileSystem;
import framework.communication.internal.persistance.IXMLCodec;
import framework.communication.internal.signal.IDataPipeline;
import framework.communication.internal.signal.SignalListenerContainer;
import framework.communication.internal.signal.arguments.EventArgs;
import framework.communication.internal.signal.arguments.PipelinedEventArgs;
import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ModelFactory;
import framework.core.system.Application;
import framework.core.system.GameValues;

import editor.models.ProjectModel;
import editor.models.TileLayerModel;
import editor.models.TileMapModel;

/**
 * Builder pattern used for saving a map, so that it can be loaded back afterwards
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public final class SaveTileMapBuilder extends AbstractBuilder<FileSystem> implements IDataPipeline<IModel> {

    @Override public boolean buildStart() {
        ProjectModel projectModel = AbstractFactory.getFactory(ModelFactory.class).get(ProjectModel.class);

        // Create a file chooser so that we can get information about
        // the save process
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + File.separator + "desktop");
        fileChooser.setSelectedFile(new File(projectModel.getName() + IXMLCodec.EXTENSION_XML));

        // Set the directory location
        if(GameValues.exists(fileChooser.getClass().getSimpleName())) {
            fileChooser.setCurrentDirectory(new File(GameValues.getGameValue(fileChooser.getClass().getSimpleName())));
        }

        // Open the file chooser so the user can navigate to the path
        // where the file should be saved.  The user will also enter a
        // file name, this is taken and will later be used to create
        // the file
        int saveValue = fileChooser.showSaveDialog(Application.instance);

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


    @Override public Map<String, SignalListenerContainer> getSignals() {
        return null;
    }

    @Override public void update(EventArgs signalEvent) {
    }
}