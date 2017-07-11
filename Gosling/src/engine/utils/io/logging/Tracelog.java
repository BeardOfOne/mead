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

package engine.utils.io.logging;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import engine.core.system.EngineProperties;
import engine.core.system.EngineProperties.Property;

/**
 * Utility class that provides functionality to log content
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 * 
 */
public final class Tracelog {
	
	/**
	 * Logger for this class, it is an anonymous logger
	 */
	private static Logger Log;

	/**
	 * The date formatter, this is used for day, month, year formatting
	 */
	private static DateFormat LogDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * The time formatter, this is used for hours, minutes, seconds formatting
	 */
	private static DateFormat LogTimeFormat = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * Flag that specifies if output to the console should be disabled
	 */
	private static boolean OutputDisabled;
	
	/**
	 * Static constructor to initialize the logging system
	 */
	static {
		
		// Get the directory for the log file
		String path = EngineProperties.instance().getProperty(Property.LOG_DIRECTORY);
		
		// If there is not a valid path then do not continue logging
		if(path == null || path.isEmpty()) {
			Tracelog.logWarning("Logging disabled, no log path has been specified");
		}
		else {
			try {
				
				// Get a file reference to the path, and make sure that the specified path
				// is a directory.  If it is a directory then make sure the folders exist
				// there by creating them.
				File file = new File(path);
				if(file.isFile()) {
					Tracelog.logError("Log files must point to a directory, not a file.");
				}
				else {
					file.mkdirs();
				}
				
				// Create a new file handler and set the title to be the current date.
				FileHandler handler = new FileHandler(path + LogDateFormat.format(new Date()) + ".txt", true);
				
				// Set the formatting option which is by default the one provided by the engine
				handler.setFormatter(new DefaultLogFormatter());
				
				// Create a new anonymous logger
				Log = Logger.getAnonymousLogger();
				
				// Add the default formatter to the log object
				Log.addHandler(handler);
				
				// Do not use the parent handler, this is to prevent
				// extra debug lines from being output to the file(s) 
				// and the display.  By doing this we are truly in full
				// control of how the output string is generated
				Log.setUseParentHandlers(false);
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
	public static void disableOutputStream(boolean disabled) {
		OutputDisabled = disabled;
	}
		
	/**
	 * Logs an error message
	 * 
	 * @param text The text to log
	 */
	public static void logError(String text) {
		Tracelog.println(Level.SEVERE, text);
	}
	
	/**
	 * Logs the specified exception
	 * 
	 * @param exception The exception to log
	 */
	public static void logException(Exception exception) {
		
		// Create a string writer and print writer to get the
		// contents of the specified exception
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		exception.printStackTrace(printWriter);
		
		// Log the exception contents
		Tracelog.println(Level.SEVERE, stringWriter.toString());
	}

	/**
	 * Logs a warning message
	 * 
	 * @param text The text to log
	 */
	public static void logWarning(String text) {
		Tracelog.println(Level.WARNING, text);
	}
	
	/**
	 * Logs generic text
	 * 
	 * @param text The text to log
	 */
	public static void log(String text) {
		Tracelog.println(Level.INFO, text);
	}
	
	/**
	 * Closes and cleans up the logger, this should be called before the application terminates
	 */
	public static void close() {
		
		// Go through the list of loggers and get handlers so 
		// that they can be closed, this will flush whatever data is
		// left within them and remove any temporary lock files
		// that are hanging around
		if(Log != null) {
			for(Handler handle : Log.getHandlers()) {
			    handle.close();   
			}
		}
	}
	
	/**
	 * Print contents using the standard output stream
	 * 
	 * @param level The level of the log
	 * @param text The text to print out
	 */
	private static void println(Level level, String text) {
		
		// Attempt to log to an output file
		if(Log != null) {
			Log.log(level, text);	
		}
		
		// Attempt to log to the console output
		if(!OutputDisabled) {
			
			// Get a reference to the proper stream
			PrintStream stream = new PrintStream(level == Level.SEVERE ? System.err : System.out);
			
			// Print the contents to the stream
			stream.println(String.format("%s [%s]:\t%s",
				LogTimeFormat.format(new Date()),
				level.toString(),
				text
			));		
			
			// Note: Do not close the stream, just flush it.
			stream.flush();
		}
	}
}