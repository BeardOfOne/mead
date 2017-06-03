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

/**
 * Class used for path related operations dealing with files of various sorts
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class Path {
	
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
	    return classType.getPackage().getName().replace(".", File.separator);
	}

}
