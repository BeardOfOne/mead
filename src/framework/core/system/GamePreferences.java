package framework.core.system;

import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import framework.utils.logging.Tracelog;

/**
 * The preferences manager that manages the persisted preferences of the application
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 * Known Issues
 *   WARNING: Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002. Windows RegCreateKeyEx(...) returned error code 5.
 *   1. Go into your Start Menu and type regedit into the search field.
 *   2. Navigate to path HKEY_LOCAL_MACHINE\Software\JavaSoft 
 *   3. Right click on the JavaSoft folder and click on `New Key`, name it Prefs
 */
public abstract class GamePreferences {

    /**
     * The preferences node of this class
     */
    protected final Preferences preferences;
    
    /**
     * Constructs a new instance of this class type
     * 
     * @param classType The class type to associate the game preferences to
     * @param <T> Any object
     */
    public <T extends Object> GamePreferences(Class<T> classType) {
        preferences = Preferences.userNodeForPackage(classType);
    }
        
    /**
     * Clears the game preferences
     */
    public void clear() {
        try {
            preferences.clear();
        } catch (BackingStoreException exception) {
            Tracelog.log(Level.SEVERE, true, exception);
        }
    }
    
    /**
     * Loads all the preferences stored within this preferences manager
     */
    public abstract void load();

    /**
     * Saves all the preferences stored within this preferences manager
     */
    public abstract void save();
}