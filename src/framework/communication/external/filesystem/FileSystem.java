package framework.communication.external.filesystem;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * File system for storing the contents of data
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
@XmlRootElement(name = "root")
public class FileSystem extends AbstractFileSystem {

    /**
     * Constructs a new instance of this class type
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
}