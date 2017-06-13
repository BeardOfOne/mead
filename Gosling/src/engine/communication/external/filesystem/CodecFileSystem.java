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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This filesystem represents the persistant filesystem for saving and loading the tilemap within the editor
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
@XmlRootElement(name = "FileSystem")
public class CodecFileSystem extends AbstractFileSystem {
		
	/**
	 * Default no-arg constructor as-per serialization guidelines dictates
	 */
	public CodecFileSystem() {
	}
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param path The path associated to this file system
	 */
	public CodecFileSystem(File file) {
		super(file);
	}

	@Override public String serialize() {
		
		// Attempt to serialize the file system
		try {
			// Create the XML codec and have it set to 
			// this class type
			XMLCodec codec = new XMLCodec(this.getClass());
			
			// Enable formatting of this object's xml
			codec.setFormatted(true);
			
			// Get the marshaller and marshal this class
			// into the file resource
			codec.getMarshaller().marshal(this, _file);
		} 
	    catch (Exception exception) {
			exception.printStackTrace();
	    }
		
		return null;
	}
}