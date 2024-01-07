package framework.communication.external.filesystem.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import framework.communication.internal.persistance.IXMLCodec;

/**
 * List wrapper used to contain a list of complex objects, used for serialization/deserialization purposes
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FileSystemWrapperList implements IXMLCodec {

    /**
     * Holds the list of file system elements
     */
    @XmlElement(name = "entry")
    public final List<FileSystemElement> ENTRIES = new ArrayList();

    /**
     * Default constructor as per serialization requirements
     */
    public FileSystemWrapperList() {
    }

    @Override public String serialize() {
        return null;
    }
}
