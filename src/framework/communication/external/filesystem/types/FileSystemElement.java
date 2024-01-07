package framework.communication.external.filesystem.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Represents a simple file system entry type, similar to an entry
 * within a mad with a simple key-value pair duo
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
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
