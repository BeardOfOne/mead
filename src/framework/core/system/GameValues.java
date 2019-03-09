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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import framework.utils.logging.Tracelog;

/**
 * Utility class for storing game related constants such as paths, names, etc.
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class GameValues {

    /**
     * The singleton instance of this class
     */
    private static GameValues _instance;
    
    /**
     * The mapping of game properties
     */
    private final Map<String, String> _gameProperties = new HashMap();

    /**
     * Constructs a new instance of this class type
     */
    private GameValues() {
    }
    
    /**
     * Gets the singleton instance of this class type
     * 
     * @return The singleton instance of this class type
     */
    private static GameValues getInstance() {
        if(_instance == null) {
            _instance = new GameValues();
        }
        
        return _instance;
    }
    
    /**
     * Adds the specified game property using the provided key
     *
     * @param key The key of the game property
     * @param value The value of the game property
     * 
     * Note: The key will be trimmed of spaces from both sides, but not the value specified
     */
    public static void addGameProperty(String key, String value) {
        if(key == null || (key = key.trim()).isEmpty()) {
            Tracelog.log(Level.WARNING, false, "Cannot add the specified game property on the account if it being null or empty");
            return;
        }
        
        getInstance()._gameProperties.put(key, value);
    }
    
    /**
     * Gets the game value of the specified key
     *
     * @param key The key of the game property
     * 
     * @return The value of the game property key provided
     * 
     * Note: The key will be trimmed of spaces from both sides
     */
    public static String getGameValue(String key) {
        if(key == null || (key = key.trim()).isEmpty()) {
            Tracelog.log(Level.WARNING, false, "Cannot get the specified game property on the account if it being null or empty");
            return null;
        }
        
        return getInstance()._gameProperties.get(key);
    }

    /**
     * Gets if the specified key exists
     * 
     * @param key The key to verify if it exists
     * 
     * @return TRUE if the key exists, FALSE otherwise
     */
    public static boolean exists(String key) {
        if(key == null || !getInstance()._gameProperties.containsKey(key)) {
            return false;
        }
        
        return true;
    }
}
