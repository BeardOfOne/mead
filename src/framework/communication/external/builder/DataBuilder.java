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
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
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