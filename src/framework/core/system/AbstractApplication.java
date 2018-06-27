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

package framework.core.system;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import framework.communication.external.builder.DataBuilder;
import framework.communication.external.builder.Director;
import framework.core.system.EngineProperties.Property;
import framework.utils.logging.Tracelog;

/**
 * This class provides the entry point for all applications to extend for their game.  
 * Your main method should be within a class that extends this class.
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public abstract class AbstractApplication extends JFrame {

    /**
     * The singleton instance of this class
     */
    private static AbstractApplication _instance;

    /**
     * Debug state of the application
     */
    private boolean _isDebug;
    
    public AbstractApplication() {
        // Pressing on the close button won't do it's default action
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
     * Initializes the singleton instance
     * 
     * @param classType The specified class type to construct
     * @param isDebug The debug state of the application
     * @param <T> AbstractApplication type
     * 
     * @return The application instance
     */
    protected static <T extends AbstractApplication> void initialize(Class<T> classType, boolean isDebug) {

        if(_instance == null) {

            // Get the start time
            long startTime = System.nanoTime();

            try {
                // Create the new instance
                _instance = classType.getConstructor().newInstance();
            }
            catch(Exception exception) {
                Tracelog.log(Level.SEVERE, false, exception);
                return;
            }

            _instance._isDebug = isDebug;

            // This is what users can hook onto before the data is actually loaded up
            _instance.onBeforeEngineDataInitialized();  

            // Load any engine data
            Tracelog.log(Level.INFO, false, "Initializing Engine Data");
            if(!EngineProperties.instance().hasDataValues()) {
                Tracelog.log(Level.WARNING, false, "Cannot load the data files, no data values specified");
            }
            else {
                
                try {
                    // Create a director and use the data builder to extract content
                    new Director(
                        new DataBuilder(EngineProperties.instance().getProperty(Property.DATA_PATH_XML))
                    ).construct();
                }
                catch (Exception exception) {
                    Tracelog.log(Level.SEVERE, false, exception);
                }
                
            }
            
            // Load the window listeners of the application
            Tracelog.log(Level.INFO, false, "Initializing Engine Listeners");
            _instance.onWindowInitialized();
            Tracelog.log(Level.INFO, false, "Initializing Engine Listeners - Completed");

            Tracelog.log(Level.INFO, false, "Initializing Engine Data - Completed");
            Tracelog.log(Level.INFO, false, "Engine Initialization Completed - " + ((System.nanoTime() - startTime) / 1000000) + "ms");
            
            _instance.setVisible(true);
        }
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
     * Called when the application is shown.  
     * 
     * Note: A shown application is when the application's visibility is TRUE
     */
    protected void onApplicationShown() {
    }
    
    /**
     * Sets the window listeners for the application
     */
    protected void onWindowInitialized() {
    
        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);
        menu.revalidate();
        menu.repaint();
        
        addComponentListener(new ComponentAdapter() {
            @Override public void componentShown(ComponentEvent event) {
                onApplicationShown();
            }
        });
    
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent windowEvent) {
                _instance.dispose();
            };
        });
    
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent event) {
                Tracelog.log(Level.INFO, false, "Powering down engine, and shutting off the application.");
                Tracelog.close();
                System.exit(0);
            }
        });
    }
    
    /**
     * Called before the data has been initialized and loaded into the game engine components
     */
    protected abstract void onBeforeEngineDataInitialized();
}