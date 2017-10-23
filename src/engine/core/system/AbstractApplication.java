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

package engine.core.system;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import engine.core.factories.AbstractFactory;
import engine.core.factories.DataFactory;
import engine.core.mvc.IDestructor;
import engine.utils.logging.Tracelog;

/**
 * This class provides the entry point for all applications to extend for their game.  
 * Your main method should be within a class that extends this class.
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public abstract class AbstractApplication extends JFrame implements IDestructor {
	
	/**
	 * The singleton instance of this class
	 */
	private static AbstractApplication _instance;
	
	/**
	 * Debug state of the application
	 */
	private boolean _isDebug;
	
	/**
	 * Constructs a new instance of this class type
	 */
    protected AbstractApplication() {
    	// Set the menu of the application
        setJMenuBar(new JMenuBar());
    }
    
    /**
     * Sets the window listeners for the application
     */
    private void initializeWindowListeners() {
		addComponentListener(new ComponentAdapter() {
		    @Override public void componentHidden(ComponentEvent event) {
		        setJMenuBar(null);
		    }
		    
		    @Override public void componentShown(ComponentEvent event) {
		        onApplicationShown();
		    	getJMenuBar().revalidate();
			    getJMenuBar().repaint();
		    }
		});
		
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosed(WindowEvent event) {
				Tracelog.log(Level.INFO, false, "Engine shutting down");
				onEngineShutdown();
				System.exit(0);
			}
		});
    }

    /**
     * Gets the singleton instance of this application
     * 
     * @return The singleton instance
     */
    public static AbstractApplication instance() {
		if(_instance == null) {
			Tracelog.log(Level.WARNING, false, "Application instance was requested but was not initialized.");
		}
		return _instance;
    }
    
    /**
     * Performs a shutdown of the application, closing all necessary components and sub-components in the process
     */
    public static void shutdown() {
	 	_instance.dispose();
    }
    
    /**
     * Initializes the singleton instance
     * 
     * @param classType The specified class type to construct
     * @param isDebug The debug state of the application
     * @param <T> AbstractApplication type
     * 
     * @throws Exception If something went wrong, please note that this calls the default constructor of your class type
     * 
     * @return The application
     */
    protected static <T extends AbstractApplication> AbstractApplication initialize(Class<T> classType, boolean isDebug) throws Exception {
	    	
		if(_instance == null) {

			// Create the new instance
    		_instance = classType.getConstructor().newInstance();
    		_instance._isDebug = isDebug;

    		// This is what users can hook onto before the data is actually loaded up
    		_instance.onBeforeEngineDataInitialized();  
    		
	    	// Load the window listeners of the application
    		Tracelog.log(Level.INFO, false, "--Engine Window Listeners Initializing--");
    		_instance.initializeWindowListeners();
    		Tracelog.log(Level.INFO, false, "--Engine Window Listeners Initialization Completed--");
    		
    		// Load any engine data
    		Tracelog.log(Level.INFO, false, "--Engine Data Initializing--");
    		_instance.loadData();
    		Tracelog.log(Level.INFO, false, "--Engine Data Initialization Completed--");	    		
    		Tracelog.log(Level.INFO, false, "--Engine Bootup Finished--");
    	}
	
    	return _instance;
    }
    
    /**
     * Gets the debug state of the application
     * 
     * @return TRUE if the application is running in a debug state, FALSE otherwise
     */
    public boolean isDebug() {
		return _isDebug;
    }

    /**
     * Loads data based on the specified engine properties data path into the 
     * abstract data factory
     */
    private void loadData() {
		try {
			// Load the data into the game
			AbstractFactory.getFactory(DataFactory.class).loadData();			
		} 
		catch (Exception exception) {
			// Print the stack trace
			exception.printStackTrace();
			
			// Exit the system with a bad error code
			System.exit(1);
		}
    }

    /**
     * Called when the engine is being shut down
     */
    private void onEngineShutdown() {
    	Tracelog.close();
    }
    
    /**
     * Sets the engine default values for the game
     */
    protected abstract void onBeforeEngineDataInitialized();
    
    /**
     * Called when the application is shown.  
     * 
     * Note: A shown application is when the application's visibility is TRUE
     */
    protected void onApplicationShown() {
    }
}