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

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import engine.api.IDestructor;
import engine.core.factories.AbstractFactory;
import engine.core.factories.DataFactory;
import engine.utils.io.logging.Tracelog;

/**
 * This class provides the entry point for all applications to extend
 * for their game.  Their main method should be within a class that extends
 * this class.
 * 
 * This class will construct the base listeners for the game, along with the engine
 * default values (pre-initialization) and other core functionality
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
	 * Constructs a new instance of this class
	 */
    protected AbstractApplication() {
    	// Set the menu of the application
        setJMenuBar(new JMenuBar());    
    }
    
    /**
     * Sets the listeners for the game
     */
    private void initializeWindowListeners() {
		addComponentListener(new ComponentAdapter() {
		    @Override public void componentHidden(ComponentEvent e) {
		        setJMenuBar(null);
		    }
		    @Override public void componentShown(ComponentEvent e) {
		        // Generate menu system
		    	setWindowedInstanceMenu();
		    	
		    	getJMenuBar().revalidate();
		        getJMenuBar().repaint();
		    }
		});
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosed(WindowEvent e) {
				Tracelog.log("Engine shutting down");
				engineShutdown();
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
    	return _instance;
    }
    
    /**
     * Performs a shutdown of the application, closing all necessary
     * components and sub-components in the process
     */
    public static void shutdown() {
    	 _instance.dispose();
    }
    
    /**
     * Initializes the singleton instance, this should be called before the {@code instance()} method
     * 
     * @param classType The specified class type to construct
     * 
     * @throws Exception If something went wrong, please note that this calls the default constructor of your class type
     */
    protected static <T extends AbstractApplication> AbstractApplication initialize(Class<T> classType) throws Exception {
    	if(_instance == null) {
    		
    		// Create the new instance
    		_instance = classType.getConstructor().newInstance();
    	
	    	// Load the engine properties, this must be done before doing anything else
    		_instance.initializeEngineProperties();
    		Tracelog.log("--Initializing Engine Properties Completed--");
    			
	    	// Load the window listeners of the application
    		_instance.initializeWindowListeners();
    		Tracelog.log("--Initializing Window Listeners Completed--");
    		
	    	// Load the engine resources of the application
    		_instance.initializeEngineResources();      
    		Tracelog.log("--Initializing Engine Resources--");
    		
    		// Load the data into the engine
    		_instance.loadData();
    		Tracelog.log("--Loading Data Completed--");
    	}
    	
    	return _instance;
    }

    /**
     * Loads data based on the specified engine properties data path into the 
     * abstract data factory
     */
    private void loadData() {
		try {
			// Load the data into the game
			AbstractFactory.getFactory(DataFactory.class).loadData();
			
		} catch (Exception exception) {

			// Print the stack trace
			exception.printStackTrace();
			
			// Exit the system with a bad error code
			System.exit(1);
		}
    }

    /**
     * Defines the Engine Shutdown procedure
     */
    private void engineShutdown() {
    	// Shut down logger
    	Tracelog.close();
    }
    
    /**
     * Sets the engine default values for the game
     * 
     * Note: This is a good place to initialize the resource manager
     * for localization.
     */
    protected abstract void initializeEngineResources();
    
    /**
     * Sets the menu functionality for the game
     */
    protected abstract void setWindowedInstanceMenu();
    
    /**
     * Initializes the engine properties
     */
    protected abstract void initializeEngineProperties();
}