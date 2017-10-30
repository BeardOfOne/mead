package engine.communication.external.filesystem;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * File system for storing the contents of data
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
@XmlRootElement(name = "FileSystem")
public class FileSystem extends AbstractFileSystem {
	
	/**
     * Default no-arg constructor as-per serialization guidelines dictates
     */
    public FileSystem() {
    }
    
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param file The path associated to this file system
	 */
	public FileSystem(File file) {
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