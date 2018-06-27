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

package framework.communication.external.filesystem;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import framework.api.IModel;
import framework.communication.external.filesystem.types.FileSystemAdapter;
import framework.communication.internal.persistance.ISerializable;
import framework.communication.internal.persistance.IXMLCodec;

/**
 * Abstract functionality for all file system implementations
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractFileSystem<T extends ISerializable<IModel>> implements IXMLCodec {

    /**
     * The data associated to this file system
     */
    @XmlElement(name = "data")
    @XmlJavaTypeAdapter(FileSystemAdapter.class)
    private final Map<Class<T>, List<T>> _data = new HashMap();

    /**
     * The file construct associated to this file system
     * Note: This file represents the file of actually serializing the file system
     */
    protected transient final File _file;

    /**
     * The extension used for png files
     */
    protected static transient final String EXTENSION_PNG = ".png";

    /**
     * The extension used for jar files 
     */
    protected static transient final String EXTENSION_JAR = ".jar";

    /**
     * The extension used for java files
     */
    protected static transient final String EXTENSION_JAVA = ".java";

    /**
     * Default no-arg constructor as-per serialization guidelines dictates
     */
    public AbstractFileSystem() {
        _file = null;
    }

    /**
     * Constructs a new instance of this class type
     * 
     * @param file The path associated to this file system
     */
    public AbstractFileSystem(File file) {
        _file = file;

        // Make the directories leading up to the file
        _file.getParentFile().mkdirs();
    }

    /**
     * Gets the data specified by the class type provided
     * 
     * @param classType The class type of the data to be returned
     * 
     * @return The list of concrete types of the type specified
     */
    public final List<T> getData(Class<T> classType) {
        List<T> data = _data.get(classType);
        return data == null ? new ArrayList<T>() : data;
    }

    /**
     * Gets the entire data set associated to the file system
     * 
     * @return The entire data set mapping
     */
    public final Map<Class<T>, List<T>> getAllData() {
        return _data;
    }

    /**
     * Removes all the data within the file system except that specified
     * 
     * @param classType The class type to keep within the file system
     */
    public final void removeDataExcept(Class<T> classType) {

        // If the file system contains the specified class type
        if(_data.containsKey(classType)) {

            // Hold a reference to the list of 
            List<T> values = _data.get(classType);   
            _data.clear();
            _data.put(classType, values);
        }
    }

    /**
     * Writes the specified data into the file system
     * 
     * @param data The data to write
     */
    public void write(T data) {

        // Get the list of data that has already been written
        List<T> dataList = _data.get(data.getClass());

        // If no data has been written, then we need to create
        // the construct so that data can be written
        if(dataList == null) {

            // Create a new list and put it into the file system
            dataList = new ArrayList();
            System.out.println("Adding entry into file system, Key = " + data.getClass().toString());
            _data.put((Class<T>) data.getClass(), dataList);
        }

        // Add the specified data
        dataList.add(data);
    }

    /**
     * Extracts the list of images based on the file associated to this file system
     * 
     * @param rows The number of rows
     * @param columns The number of columns
     * 
     * @return A list of images associated to the partitions of the entire image
     */
    public List<BufferedImage> extractImages(int rows, int columns) {

        List<BufferedImage> images = new ArrayList();

        try {

            BufferedImage buffer = ImageIO.read(_file);
            Dimension dimensions = new Dimension(buffer.getWidth(), buffer.getHeight());

            for(int i = 0, size = rows * columns; i < size; ++i) {

                // get the current row and column that we are currently on
                int row = i / columns;
                int column = i % columns;

                // Set the top-left point
                int x = column * (dimensions.width / columns);
                int y = row * (dimensions.height / rows);

                BufferedImage image = buffer.getSubimage(x, y, dimensions.width / columns, dimensions.height / rows);

                images.add(image);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return images;
    }

    @Override public String serialize() {

        // Serialize this class's contents
        String serializedData = IXMLCodec.super.serialize();

        // try-with-resource the printwriter statement
        try(PrintWriter out = new PrintWriter(_file)) {

            // output the contents of the serialized data into the file
            out.println(serializedData);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return serializedData;
    }
}