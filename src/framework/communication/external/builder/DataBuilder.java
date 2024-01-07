package framework.communication.external.builder;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import framework.api.IData;
import framework.communication.external.filesystem.FileSystem;
import framework.communication.internal.persistance.IXMLCodec.XMLCodec;
import framework.core.factories.AbstractFactory;
import framework.core.factories.DataFactory;
import framework.utils.logging.Tracelog;

/**
 * Data builder used for building data from the editor and
 * other data places
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class DataBuilder extends AbstractBuilder<FileSystem> {

    /**
     * The path where the data resides
     */
    private final String _path;

    /**
     * Constructs a new instance of this class type
     * 
     * @param path The path where the data resides
     */
    public DataBuilder(String path) {
        _path = path;
    }

    @Override public boolean buildStart() {

        // Create the codec for the file system
        XMLCodec codec = XMLCodec.createInstance(FileSystem.class);

        // Reference the data file
        try(InputStream inStream = getClass().getResourceAsStream(_path)) {
            // Get the selected map file and unmarshal it
            Object fileSystem = codec.getUnmarshaller().unmarshal(inStream);

            // Create the file system 
            _fileSystem = (FileSystem)fileSystem;
        }
        catch (Exception exception) {
            Tracelog.log(Level.SEVERE, false, exception);
            return false;
        }

        return true;
    }

    @Override public void buildContent() {
        // Get the list of values in the file system
        Collection<Collection<IData>> collectionSet = _fileSystem.getAllData().values();

        // Get the data factory
        DataFactory factory = AbstractFactory.getFactory(DataFactory.class);
        
        // Go through the list of data within the collection
        for(Iterator<Collection<IData>> iterator = collectionSet.iterator(); iterator.hasNext();) {
            factory.populateData(iterator.next());
        }
    }

    @Override public void buildEnd() {
    }
}