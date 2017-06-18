package engine.communication.external.filesystem;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * File system for storing the contents of data
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
@XmlRootElement(name = "FileSystem")
public class DataFileSystem extends AbstractFileSystem {
	
	/**
     * Default no-arg constructor as-per serialization guidelines dictates
     */
    public DataFileSystem() {
    }
    
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param file The path associated to this file system
	 */
	public DataFileSystem(File file) {
		super(file);
	}
}