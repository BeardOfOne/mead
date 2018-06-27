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
package framework.utils.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Represents a single instance of a JAR package
 *
 * Note: Make sure you call {@code close() } on this class object when you
 *       are finished adding entries
 *       
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
public class JarPackage {

    /**
     * The manifest of this jar package
     */
    private final Manifest _manifest = new Manifest() {{
        getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
    }};

    /**
     * The output stream referencing the jar file
     */
    private JarOutputStream _jarOutputStream = null;

    /**
     * Constructs a new instance of this class type
     * 
     * @param path The destination path for where the .jar file should be
     */
    public JarPackage(String path) {
        try {
            _jarOutputStream = new JarOutputStream(new FileOutputStream(path), _manifest);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Adds the provided data into the jar file at the location within the jar file specified
     * 
     * @param data The data that will be written into the jar file.
     * @param location The location within the jar where the written data will reside
     */
    public void addEntry(File data, String location) {

        // Create the stream for writing to the jar file and reading the contents of the file specified
        try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(data))) {

            // Create a jar entry, make sure that the paths are properly formatted
            JarEntry entry = new JarEntry(location.replace("\\", "/") + data.getName());

            // Set the last modified entry
            entry.setTime(data.lastModified());

            // Apply the entry into the stream
            _jarOutputStream.putNextEntry(entry);

            // read the contents of the button and write to the target stream
            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1) {
                    break;
                }
                _jarOutputStream.write(buffer, 0, count);
            }

            // Close the jar entry object
            _jarOutputStream.closeEntry();
        }
        catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Closes the underlying stream
     */
    public void close() {
        try {
            _jarOutputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}