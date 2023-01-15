/**
 * Daniel Ricci {@literal <thedanny09@icloud.com>}
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
import java.time.Duration;
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
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class Application extends JFrame {

    /**
     * The flag indicating if the application is restarting
     */
    public boolean isRestarting;
    
    /**
     * The singleton instance
     */
    public static Application instance;
    
    /**
     * Debug state of the application
     */
    public final boolean isDebug;

    /**
     * The default application name of this application
     */
    protected String _applicationName;
    
    /**
     * The running time of engine
     */
    private long runningTime = System.nanoTime();
    
    /**
     * Constructs a new instance of this class type
     *
     * @param isDebug If the application is in debug mode
     */
    protected Application(boolean isDebug) {
        
        instance = this;
        
        // Pressing on the close button won't do it's default action
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        
        this.isDebug = isDebug;
    
        // TODO - This method should not be overriden because i dont want the child to have code executing until
        //        The construction is done
        onBeforeEngineDataInitialized();
        
        // Get the start time
        long startTimeMain = System.nanoTime();
        
        // Load any engine data
        Tracelog.log(Level.INFO, false, "Initializing Engine Started");
            
        // Load any engine data
        Tracelog.log(Level.INFO, false, "Load Data Entities Started");
        if(!EngineProperties.instance().hasDataValues()) {
            Tracelog.log(Level.WARNING, false, "Cannot load the data files, no data values specified");
        }
        else {
            // Get the start time
            long startTimeData = System.nanoTime();
            
            try {
                // Create a director and use the data builder to extract content
                new Director(
                    new DataBuilder(EngineProperties.instance().getProperty(Property.DATA_PATH_XML))
                ).construct();
            }
            catch (Exception exception) {
                Tracelog.log(Level.SEVERE, false, exception);
            }
            finally {
                Tracelog.log(Level.INFO, false, "Load Data Entities Finished - " + ((System.nanoTime() - startTimeData) / 1000000) + "ms");
            }
        }
        Tracelog.log(Level.INFO, false, "Engine Initialization Finished - " + ((System.nanoTime() - startTimeMain) / 1000000) + "ms");
            
        // TODO - This method should not be overriden because i dont want the child to have code executing until
        //        The construction is done
        onWindowInitialized();
        
        // Notify on the title that the game is in debug mode
        if(isDebug) {
            Tracelog.log(Level.INFO, false, "In Debug Mode");
        }
    }
    
    /**
     * This method gets called when the game is being restarted
     */
    public void onRestart() {
        isRestarting = false;
    }

    public void setTitle(String projectName) {
        super.setTitle(_applicationName + " - " + projectName);
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
                dispose();
            };
        });
    
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent event) {
                Tracelog.log(Level.INFO, false, "Engine Running Time - " + formatDuration(Duration.ofMillis(((System.nanoTime() - runningTime) / 1000000))));
                Tracelog.log(Level.INFO, false, "Powering down engine, and shutting off the application.");
                Tracelog.close();
                System.exit(0);
            }
        });
    }
    
    /**
     * Formats the specified duration to H:MM:SS 
     *
     * @param duration The duration
     * 
     * @return The formatting duration as a string representation
     */
    private static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
            "%d:%02d:%02d",
            absSeconds / 3600,
            (absSeconds % 3600) / 60,
            absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
    
    /**
     * Called before the data has been initialized and loaded into the game engine components
     */
    protected void onBeforeEngineDataInitialized() {
    }

    @Override public String getTitle() { 
        return _applicationName; 
    }
}