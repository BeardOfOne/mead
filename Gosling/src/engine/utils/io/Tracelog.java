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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import engine.core.system.EngineProperties;
import engine.core.system.EngineProperties.Property;

/**
 * Utility class that provides functionality to log content
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 * 
 */
public final class Tracelog {
	
	private static Logger Logger = java.util.logging.Logger.getLogger("");

	
	/**
	 * The date formatter, this is used for day, month, year formatting
	 */
	private static DateFormat LogDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * The time formatter, this used for time-specific logging
	 */
	//private static DateFormat LogTimeFormat = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * The current date
	 */
	private static Date Date = new Date();
		
	/**
	 * The vanilla output stream of the application (the original System.out)
	 */
	private static final PrintStream OutStream = System.out;
	
	/**
	 * Writer for writing to a text file
	 */
	private BufferedWriter _writer;

	/**
	 * Flag that specifies if output to the console should be disabled
	 */
	private static boolean OutputDisabled;
	
	/**
	 * The singleton instance representation
	 */
	private static Tracelog _instance = new Tracelog();
	
	/**
	 * Constructs a new instance of this class type
	 */
	private Tracelog() {
		String path = EngineProperties.instance().getProperty(Property.LOG_DIRECTORY);
		if(path == null || path.isEmpty()) {
			System.out.println("Logging disabled, no log path has been specified");
		}
		else {
			try {
				FileHandler FileHandler = new FileHandler(path + LogDateFormat.format(Date) + ".txt", true);
				FileHandler.setFormatter(new SimpleFormatter());
				Logger.addHandler(FileHandler);
				
				// TODO - Check against the current date when writing the log, this should
				// close the stream, create a new file with the proper date.
				// Create a new file and format it based on the current date.
				File file = new File(path + LogDateFormat.format(Date) + ".txt");
				_writer = new BufferedWriter(new FileWriter(file));
				System.out.println("Log file saved at location" + file.getAbsolutePath());
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Indicates if output on the console display should be disabled 
	 * 
	 * @param disabled If output should be disabled
	 */
	public void disableOutputStream(boolean disabled) {
		OutputDisabled = disabled;
	}
		
	/**
	 * Logs an error message
	 * 
	 * @param text The text to log
	 */
	public static void logError(String text) {
		//Tracelog.println("Error:\t" + text);
	}

	/**
	 * Logs a warning message
	 * 
	 * @param text The text to log
	 */
	public static void logWarning(String text) {
		//Tracelog.println("Warning:\t" + text);
	}
	
	/**
	 * Logs generic text
	 * 
	 * @param text The text to log
	 */
	public static void log(String text) {
		Tracelog.println(Level.INFO, text);
		_instance.writeln(text);
	}
	
	/**
	 * Print contents using the standard output stream
	 * 
	 * @param text The text to print out
	 */
	private static void println(Level level, String text) {
		if(!OutputDisabled) {
			Logger.log(level, text);
		}
	}
	
	/**
	 * Writes the specified text to the currently held write stream
	 * 
	 * @param text The text to write out
	 */
	private void writeln(String text) {
		if(_writer != null) {
			try {
				_writer.write(text);
				_writer.newLine();
			} 
			catch (IOException exception) {
				exception.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Closes and cleans up the logger, this should be called before the application terminates
	 */
	public static void close() {
		for(Handler handle : Logger.getHandlers()) {
		    handle.close();   
		}
	}
}