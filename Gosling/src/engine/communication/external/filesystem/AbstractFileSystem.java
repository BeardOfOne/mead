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

package engine.communication.external.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import engine.api.IModel;
import engine.communication.internal.persistance.ISerializable;

/**
 * Abstract functionality for all file system implementations
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractFileSystem<T extends ISerializable<IModel>> {
	
	/**
	 * The data associated to this file system
	 */
	@XmlElement(name = "data")
	@XmlJavaTypeAdapter(FileSystemAdapter.class)
	private final Map<Class<T>, List<T>> _data = new HashMap();
	
	/**
	 * The file construct associated to this file system
	 */
	protected transient final File _file;
		 
	/**
	 * Default no-arg constructor as-per serialization guidelines dictates
	 */
	public AbstractFileSystem() {
		_file = null;
	}
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param file The path associated to this file system
	 */
	public AbstractFileSystem(File file) {
		_file = file;
	}

	/**
	 * Gets the data specified by the class type provided
	 * 
	 * @param classType The class type of the data to be returned
	 * 
	 * @return The list of concrete types of the type specified
	 */
	public final <U extends T> List<U> getData(Class<U> classType) {
		return (List<U>) _data.get(classType);
	}
	
	/**
	 * Writes the specified data into the file system
	 * 
	 * @param data The data to write
	 */
	public final void write(T data) {
		
		// Get the list of data that has already been written
		List<T> dataList = _data.get(data.getClass());
		
		// If no data has been written, then we need to create
		// the construct so that data can be written
		if(dataList == null) {
			
			// Create a new list and put it into the file system
			dataList = new ArrayList();
			System.out.println("Adding entry into file system, Key = " + data.getClass().toString());
			_data.put((Class<T>) data.getClass(), dataList);
		}
		
		// Add the specified data
		dataList.add(data);
	}
}
