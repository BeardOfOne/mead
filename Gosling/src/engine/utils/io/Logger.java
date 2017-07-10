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
import java.io.IOException;
import java.io.PrintStream;

import engine.core.system.EngineProperties;
import engine.core.system.EngineProperties.Property;

/**
 * Utility class that provides functionality to log content
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 * 
 */
public class Logger {
	
	/**
	 * The vanilla output stream of the application (the original System.out)
	 */
	private final PrintStream _stream = System.out;
	
	private FileWriter _writer;
	
	/**
	 * The singleton instance representation
	 */
	private static Logger _instance;
	
	/**
	 * Constructs a new instance of this class type
	 */
	private Logger() {
		String path = EngineProperties.instance().getProperty(Property.LOG_PATH);
		if(path == null || path.isEmpty()) {
			System.out.println("Logging disabled, no log path has been specified");
		}
		else {
			try {
				_writer = new FileWriter(new File(path));
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Gets a singleton instance reference of this class type
	 * 
	 * @return The singleton instance reference of this class type
	 */
	public static Logger getInstance() {
		if(_instance == null) {
			_instance = new Logger();
		}

		return _instance;
	}
	
	public void LogError(String text) {
		_stream.println(text);
	}
	
	public void LogWarning(String text) {
		_stream.println(text);
	}
	
	public void Log(String text) {
		_stream.println(text);
	}
}