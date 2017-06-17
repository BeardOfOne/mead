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

package engine.utils.io;

import java.io.File;
import java.net.URL;

/**
 * Class used for path related operations dealing with files of various sorts
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class Paths {
	
    /**
     * Helper method used to treat string as extensions based on the specified
     * flags
     * 
     * @param extension The extension string
     * @param supressExtension if the extension should be included or not
     * 
     * @return The extension string filtered by the specified flag
     */
    public static String cleanExtensionMark(String extension, boolean supressExtension) {
        if(extension.indexOf('.') == 0) {
            return supressExtension ? extension.substring(1, extension.length()) : extension;
        }
        return supressExtension ? extension : '.' + extension;
    }
    
	/**
	 * Helper method used to obtain the filename of a file without it's extension
	 * 
	 * @param file The file object
	 * 
	 * @return The name of the file without the extension
	 */
	public static String filenameNoExtension(File file) {
		
		// Get the index of the last occurring '.' (dot) character
		int index = file.getName().lastIndexOf('.');
		
		// return the substring of the word from the beginning of the name of the file
		// until the dot (non-inclusive), or just the entire name if there is no dot found
		return file.getName().substring(0, index == -1 ? file.getName().length() : index);
	}
	
	/**
	 * Converts the class package name to the logical path on disk
	 * 
	 * @param classType The class type of the class to be converted
	 * 
	 * @return The logical absolute path associated to the class type specified
	 */
	public static String packageToPath(Class classType) {
	    String name = classType.getPackage().getName();
	    return name.indexOf(".") == -1 ? name + File.separator : name.replace(".", File.separator);
	}
	
	/**
	 * Gets the absolute path of the specified class type
	 * 
	 * @param classType The class type to locate on disk
	 * 
	 * @return The absolute path of the specified class type
	 */
	public static String getClassLocation(Class classType) {
		
		// Get the name of the class
		String name = getClassName(classType);
				
		// Get a reference to the path of the file
		URL resourcePath = classType.getResource(name + ".class"); 
		
		// return the absolute path of the resource found
		return resourcePath.getFile();
	}
	
	/**
	 * Gets the name of the class type specified
	 * 
	 * @param classType The specified class type
	 * 
	 * @return The name of the class type specified
	 */
	public static String getClassName(Class classType) {
		// Get the name of the class
		String name = classType.getName();
		
		// Get the last index of the . within the string since
		// there could be package references (package1.package2...)
		int index = name.lastIndexOf('.');

		// If an index was found
		if(index != -1) {
			
			// Take the contents of the string after the last dot
			// until the end (ClassN
			name = name.substring(index + 1, name.length());
		}
		
		return name;
	}
	
	/**
	 * Gets the name of the class type specified
	 * 
	 * @param classType The specified class type
	 * @param includeExtension Specify if the extension should be included in the query
	 * 
	 * @return The name of the class type specified
	 */
	public static String getClassName(Class classType, boolean includeExtension) {
		
		// Return the name of the class, and if the class extension is specified then include it
		return String.format("%s%s", getClassName(classType), includeExtension ? ".class" : "");
	}
}
