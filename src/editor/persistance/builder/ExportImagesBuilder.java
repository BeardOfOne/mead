package editor.persistance.builder;

import java.io.File;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import framework.api.IModel;
import framework.communication.external.builder.AbstractBuilder;
import framework.communication.internal.signal.IDataPipeline;
import framework.communication.internal.signal.SignalListenerContainer;
import framework.communication.internal.signal.arguments.EventArgs;
import framework.communication.internal.signal.arguments.PipelinedEventArgs;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ModelFactory;
import framework.core.system.Application;
import framework.core.system.GameValues;
import framework.utils.globalisation.Localization;

import editor.models.TileMapModel;
import editor.persistance.filesystem.ImageFileSystem;
import resources.ResourceKeys;

/**
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 */
public final class ExportImagesBuilder extends AbstractBuilder<ImageFileSystem> implements IDataPipeline<IModel> {

    /**
     * Helper method used to create a folder. This method will prompt the user to chose
     * the location of the folder for creation.
     * 
     * Note: This method involves user interaction
     * 
     * @param fileChooser The file chooser object, properly configured beforehand
     *  
     * @return If the folder operation was a success
     */
    private File createExportFolder(JFileChooser fileChooser) {

        // Set the directory location
        if(GameValues.exists(fileChooser.getClass().getSimpleName())) {
            fileChooser.setCurrentDirectory(new File(GameValues.getGameValue(fileChooser.getClass().getSimpleName())));
        }
        
        // Show the save dialog location
        if(fileChooser.showSaveDialog(Application.instance) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        
        // Store within the game properties the selected directory
        GameValues.addGameProperty(fileChooser.getClass().getSimpleName(), fileChooser.getCurrentDirectory().toString());

        // Attempt to create the directory structure
        if(fileChooser.getSelectedFile().mkdirs()) {
            return fileChooser.getSelectedFile();
        } 

        // Display to the user that the directory already existed, and that
        // contents within the directory will be overwritten
        return JOptionPane.showConfirmDialog(
                Application.instance,
                Localization.instance().getLocalizedString(ResourceKeys.ExportOverwriteMessage),
                Localization.instance().getLocalizedString(ResourceKeys.Export),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE ) == JOptionPane.YES_OPTION ? fileChooser.getSelectedFile() : null;
    }

    @Override public boolean buildStart() {

        // Initialize the file choose to the desktop of the user
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + File.separator + "desktop");

        // Filter only directories within the file choose
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	

        // Remove the all files filter
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        // Get the file location of the export folder
        File file = createExportFolder(fileChooser);
        if(file != null) {
            _fileSystem = new ImageFileSystem(file);
        } 
        else {
            _fileSystem = null;
        }

        // If no file system is created then the export folder was cancelled
        return _fileSystem != null;
    }

    @Override public void buildContent() {
        // Pipe the list of tile map models
        AbstractSignalFactory.getFactory(ModelFactory.class).multicastSignalListeners(
            TileMapModel.class, 
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