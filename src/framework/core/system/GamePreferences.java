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

import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import framework.utils.logging.Tracelog;

/**
 * The preferences manager that manages the persisted preferences of the application
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
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
     */
    public GamePreferences(Class<?> classType) {
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
    
    public abstract void load();

    /**
     * Saves all the preferences stored within this preferences manager
     */
    public abstract void save() ;
}