package framework.core.system;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
public class Application extends JFrame implements WindowListener {

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
        
        addWindowListener(this);
        
        // Pressing on the close button won't do it's default action
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        
        this.isDebug = isDebug;
            
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
    
    @Override public void windowOpened(WindowEvent windowEvent) {
    	JMenuBar menu = new JMenuBar();
	    setJMenuBar(menu);
	    menu.revalidate();
	    menu.repaint();
            
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

	@Override public void windowClosing(WindowEvent windowEvent) {
	}

	@Override public void windowClosed(WindowEvent windowEvent) {
	}

	@Override public void windowIconified(WindowEvent windowEvent) {
	}

	@Override public void windowDeiconified(WindowEvent windowEvent) {
	}

	@Override public void windowActivated(WindowEvent windowEvent) {
	}

	@Override public void windowDeactivated(WindowEvent windowEvent) {
	}
}