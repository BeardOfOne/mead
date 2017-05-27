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

package engine.communication.internal.persistance;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

//---------------------------------------------------------
/**
 * Defines a codec for performing serialization and deserialization functionality
 * using XML
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 */
//---------------------------------------------------------
public interface IXMLCodec extends ISerializable<String> {
	
	/**
	 * File extension representation for all implementors of this codec
	 */
	public static final String FILE_EXTENSION = ".xml";
	
	//---------------------------------------------------------
	/**
	 * Class that provides codec related functionality 
	 * for JAXB compatible classes
	 * 
     * @author Daniel Ricci <thedanny09@gmail.com>
	 */
	//---------------------------------------------------------
	public final class XMLCodec {
		
		/**
		 * The JAXB Context
		 */
    	private JAXBContext _context;
    	
    	/**
    	 * The Marshaller associated to the JAXB Context
    	 */
		private Marshaller _marshaller;
		
		/**
		 * The unmarshaller associated to the JAXB Context
		 */
		private Unmarshaller _unmarshaller;
				
		//---------------------------------------------------------
		/**
		 * Constructs a new codec
		 * 
		 * @param classType The type of class associated to the codec
		 * 
		 * @throws JAXBException This exception will be thrown if there is a problem in
		 * 						 the JAXB creation/setting process
		 */
		//---------------------------------------------------------
    	public XMLCodec(Class classObject) throws JAXBException {

    		// Create the JAXB context with the specified class
    		_context = JAXBContext.newInstance(classObject);

    		// Create a marshaller with our context
    		_marshaller = _context.createMarshaller();

    		// Create the unmarshaller with our context
    		_unmarshaller = _context.createUnmarshaller();
    	}
    	
		//---------------------------------------------------------
    	/**
    	 * Sets the formatting state of the JAXB context
    	 * 
    	 * @param isFormatted If formatting should be done
    	 * @throws PropertyException 
    	 */
		//---------------------------------------------------------
    	public void setFormatted(boolean isFormatted) throws PropertyException {
			_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, (Boolean)isFormatted);
    	}

    	//---------------------------------------------------------
    	/**
    	 * Gets the marshaller associated to this context
    	 * 
    	 * @return The marshaller associated to the context
    	 */
    	//---------------------------------------------------------
    	public Marshaller getMarshaller() {
    		return _marshaller;
    	}

    	//---------------------------------------------------------
    	/**
    	 * Gets the unmarshaller associated to this context
    	 * 
    	 * @return The unmarshaller associated to the context
    	 */
    	//---------------------------------------------------------
    	public Unmarshaller getUnmarshaller() {
    		return _unmarshaller;
    	}
	}
}