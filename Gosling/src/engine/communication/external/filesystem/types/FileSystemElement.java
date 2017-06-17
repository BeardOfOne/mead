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

package engine.communication.external.filesystem.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Represents a simple file system entry type, similar to an entry
 * within a mad with a simple key-value pair duo
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public final class FileSystemElement {
	
	/**
	 * The class name string representation of the class value
	 */
	@XmlElement(name = "key")
	public final String className;
	
	/**
	 * The class value representation as a string or some string value
	 */
	@XmlElement(name = "value")
	public final String classValue;
	
	/**
	 * Default constructor as per serialization requires
	 */
	public FileSystemElement() {
		className = "";
		classValue = "";
	}
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param className The name of the class as a string
	 * @param classValue The value as a string
	 */
	public FileSystemElement(String className, String classValue) {
		this.className = className;
		this.classValue = classValue;
	}
}
