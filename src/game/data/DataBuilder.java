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

package game.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import engine.api.IData;
import engine.communication.external.builder.AbstractBuilder;
import engine.communication.external.filesystem.FileSystem;
import engine.communication.internal.persistance.IXMLCodec.XMLCodec;
import engine.core.factories.AbstractFactory;
import engine.core.factories.DataFactory;

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
		try {
			// Create the codec for the file system
			XMLCodec codec = new XMLCodec(FileSystem.class);
				
			// Reference the data file
			InputStream inStream = getClass().getResourceAsStream(_path);
			
			// Get the selected map file and unmarshal it
			Object fileSystem = codec.getUnmarshaller().unmarshal(inStream);
			
			// Create the file system 
			_fileSystem = (FileSystem)fileSystem;
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
		
		// Return true to indicate everything went well
		return true;
	}
	
	@Override public void buildContent() {
		// Get the list of values in the file system
		Collection<ArrayList<IData>> collectionSet = _fileSystem.getAllData().values();
		
		// Go through the list of data within the collection
		for(Iterator<ArrayList<IData>> iterator = collectionSet.iterator(); iterator.hasNext();) {
			
			ArrayList<IData> dataList = iterator.next();
			
			// Queue the data in the data factory
			AbstractFactory.getFactory(DataFactory.class).addDataResources(dataList);				
		}		
	}

	@Override public void buildEnd() {
	}
}