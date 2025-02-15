package framework.communication.internal.persistance;

import java.io.StringWriter;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import framework.utils.logging.Tracelog;

/**
 * Defines a codec for performing serialization and deserialization functionality using XML
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public interface IXMLCodec extends ISerializable<String> {

    /**
     * File extension representation for all implementors of this codec
     */
    public static final String EXTENSION_XML = ".xml";

    /**
     * Class that provides codec related functionality for JAXB compatible classes
     * 
     * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
     */
    public final class XMLCodec {

        /**
         * The JAXB Context
         */
        private final JAXBContext _context;

        /**
         * The marshaller associated to the JAXB Context
         */
        private final Marshaller _marshaller;

        /**
         * The unmarshaller associated to the JAXB Context
         */
        private final Unmarshaller _unmarshaller;

        /**
         * 
         * Constructs a new instance of this class type
         *
         * @param classObject The type of class to serialize/deserialize
         * 
         * @throws JAXBException If an error occured during the instance creation 
         */
        protected XMLCodec(Class classObject) throws JAXBException {
            _context = JAXBContext.newInstance(classObject);
            _marshaller = _context.createMarshaller();
            _unmarshaller = _context.createUnmarshaller();
        }

        /**
         * Creates a new instance of this class type
         *
         * @param classObject The class object type
         * 
         * @return An instance of the XML codec class
         */
        public static XMLCodec createInstance(Class classObject) {
            try {
                return new XMLCodec(classObject);
            }
            catch(Exception exception) {
                Tracelog.log(Level.SEVERE, false, exception);
                return null;
            }
        }

        /**
         * Sets the formatting state of the JAXB context
         * 
         * @param isFormatted If formatting should be done
         * 
         * @throws PropertyException An exception will be thrown if the property could not be set
         */
        public void setFormatted(boolean isFormatted) throws PropertyException {
            _marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, isFormatted);
        }

        /**
         * Gets the marshaller associated to this context
         * 
         * @return The marshaller associated to the context
         */
        public Marshaller getMarshaller() {
            return _marshaller;
        }

        /**
         * Gets the unmarshaller associated to this context
         * 
         * @return The unmarshaller associated to the context
         */
        public Unmarshaller getUnmarshaller() {
            return _unmarshaller;
        }
    }

    @Override default String serialize() {
        // Create a string buffer for the xml data
        StringWriter writer = new StringWriter();
        try {
            // Create the XML codec
            XMLCodec serializer = new XMLCodec(this.getClass());
            serializer.setFormatted(true);

            // Get the marshaller and serialize this class
            serializer.getMarshaller().marshal(this, writer);
        } 
        catch (Exception exception) {
            Tracelog.log(Level.SEVERE, false, exception);
        }

        return writer.toString();
    }
}