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
import java.io.FileWriter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class handles creating and generation of code within a specified file.  The code should be to spec
 * with the Java language
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 */
public class ClassGenerator {
	
	/**
	 * The name of the class
	 */
	private final String _className;
	
	/**
	 * The name of the package
	 */
	private final String _packageName;
		
	/**
	 * The string builder for the entirety of the generation
	 */
	private final StringBuilder _classBuilder = new StringBuilder();
	
	/**
	 * The string builder for all the enumerations
	 */
	private final StringBuilder _enumBuilder = new StringBuilder();
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param packageName The name of the package
	 * @param className The name of the class
	 */
	public ClassGenerator(String packageName, String className) {
		_packageName = packageName;
		_className = className;
	}
	
	/**
	 * Appends a new line to the specified builder
	 * 
	 * @param builder The builder to append a new line into
	 */
	private void appendNewLine(StringBuilder builder) {
		builder.append(System.getProperty("line.separator"));
	}
	
	/**
	 * Resets the builders within this class
	 */
	public void reset() {
		_classBuilder.setLength(0);
		_enumBuilder.setLength(0);
	}
	
	/**
	 * Appends an enum entry into this class generator
	 * 
	 * @param enumName The name of the enum
	 * @param values The values of the enum 
	 */
	public void appendEnum(String enumName, List<UUID> values) {
    	_enumBuilder.append(String.format(
    		"\tpublic enum %s { %s }", enumName, values.stream().map(z -> z.toString()).collect(Collectors.joining(",")))
		);
    	appendNewLine(_enumBuilder);
	}
	
	/**
	 * Writes the contents of this generated class to the specified file
	 * 
	 * @param file The file to write into
	 */
	public void write(File file) {
		try(FileWriter writer = new FileWriter(file))  {
			
			// Clear the contents of the class builder before proceeding
			_classBuilder.setLength(0);
			
			// Package name;
			_classBuilder.append(String.format("package %s;", _packageName));
			appendNewLine(_classBuilder);

			// public class name {
    		_classBuilder.append(String.format("public class %s {", _className));
    		appendNewLine(_classBuilder);
			
    		// All the enum declarations and its values are included
			_classBuilder.append(_enumBuilder.toString());
			appendNewLine(_classBuilder);
			
			// The ending curly brace for the class declaration
			_classBuilder.append("}");
			
			// Write the contents of the builder to the file specified
			writer.write(_classBuilder.toString());
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}
}