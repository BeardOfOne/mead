/**
 * Daniel Ricci <thedanny09@icloud.com>
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

package framework.communication.external.filesystem.types;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import framework.communication.internal.persistance.ISerializable;
import framework.communication.internal.persistance.IXMLCodec.XMLCodec;
import framework.utils.logging.Tracelog;

/**
 * This class represents an adapter for the base file system class structure, used to convert the complex
 * file system structure field types into something more basic
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 * @param <T> A serializable typed object
 */
public class FileSystemAdapter<T extends ISerializable<String>> extends XmlAdapter<FileSystemWrapperList, Map<Class<T>, List<T>>> {

    @Override public FileSystemWrapperList marshal(Map<Class<T>, List<T>> data) throws Exception {

        // Create a new file system map type
        FileSystemWrapperList myMapType = new FileSystemWrapperList();

        // Go through the key-value pairs of the map
        for(Entry<Class<T>, List<T>> entry : data.entrySet()) {

            // Go through the element within the list of the value
            for(T value : entry.getValue()) {

                // Create a new type, and within that type store the class name 
                // and the actual value as a serialized string
                FileSystemElement entryType =  new FileSystemElement(
                        entry.getKey().getName(),
                        value == null ? "" : value.serialize()
                        );

                Tracelog.log(Level.INFO, true, String.format("Serialize Key: %s", entryType.className));
                Tracelog.log(Level.INFO, true, String.format("Serialize Value: %s", entryType.classValue));

                // Add the new object into our new map type list
                myMapType.ENTRIES.add(entryType);

            }
        }

        // Return the newly populated map type
        return myMapType;
    }

    @Override public Map<Class<T>, List<T>> unmarshal(FileSystemWrapperList data) throws Exception {

        // Create the structure to store the specified information
        Map<Class<T>, List<T>> structure = new HashMap();

        // Go through each data entry element
        for(FileSystemElement element : data.ENTRIES) {

            // Get the class type from its string association
            Class<T> classType = (Class<T>) Class.forName(element.className);

            // If the structure does not have an entry for the specified class type
            // then create a new entry with an empty list
            if(!structure.containsKey(classType)) {

                // Create the entry with an empty list
                structure.put(classType, new ArrayList<T>());
            }

            // Create a codec for unmarshalling, using the class type
            // to perform the unmarshalling 
            XMLCodec codec = XMLCodec.createInstance(classType);

            // Create a string stream and insert the XML contents of the already
            // serialized class
            StringReader reader = new StringReader(element.classValue);

            // Get the codec's unmarshaller and inject the string stream into the 
            // unmarshaller, casting back to the specified object serializable type
            T entity = (T) codec.getUnmarshaller().unmarshal(reader);

            // Insert the entity into the structure
            structure.get(classType).add(entity);
        }

        // return the structure back to the adapter caller
        return structure;
    }	
}