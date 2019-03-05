/**
 * Daniel Ricci <thedanny09@icloud.com>
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

package framework.utils.globalisation;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import framework.core.system.EngineProperties;
import framework.core.system.EngineProperties.Property;
import framework.utils.logging.Tracelog;

/**
 * Defines the functionality for providing localization in an application 
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class Localization {

    /**
     * The singleton instance of this class type
     */
    private static Localization _instance;

    /**
     * The resources mapping list.  The key is the key column in the resources file, and the value is
     * whatever the localized string is.
     */
    private final Map<String, String> _resources = new HashMap();

    /**
     * Constructs a new instance of this class type
     */
    private Localization() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(EngineProperties.instance().getProperty(Property.LOCALIZATION_PATH_CVS)))))  {
            // Hack to skip the first row, which is the header.  This will eventually be moved
            // when multi language is supported
            reader.readLine();

            // Note: Right now, the only supported language is whatever is in the second
            //       column of the .csv.  Eventually there will be support for more than one language
            String line = null;
            while((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                _resources.put(data[0], data[1]);
            }
        }
        catch(Exception exception) {
            Tracelog.log(Level.SEVERE, false, exception);
        }
    }

    /**
     * Gets the singleton instance reference of this class type
     * 
     * @return The singleton reference of this class type
     */
    public static synchronized Localization instance() {
        if(_instance == null) {
            _instance = new Localization();
        }
        return _instance;        
    }

    /**
     * Gets the localized data of the specified key
     *  
     * Note: This will toString() the provided enum.
     * 
     * @param key The key to use for lookup
     * 
     * @return The localized string
     */
    public String getLocalizedString(Enum key) {
        return getLocalizedString(key.toString());
    }

    /**
     * Gets the localized string of the provided key
     * 
     * @param key The key to use for lookup
     * 
     * @return The localized string
     */
    public String getLocalizedString(String key) {
        String result = _resources.get(key);
        if(result == null) {
            if(EngineProperties.instance().getIsPropertyValid(Property.DISABLE_TRANSLATIONS_PLACEHOLDER)) {
                result = key;
            }
            else {
                result = "<!__/////" + key.toUpperCase() + "//////!>";	
            }
        }
        
        return result;            
    }

    /**
     * Gets the localized data of the specified key
     * 
     * Note: This will toString() the provided enum.
     * 
     * @param key The key to use for lookup.
     * 
     * @return The image of the specified key
     */
    public Image getLocalizedData(Enum key) {
        return getLocalizedData(key.toString());
    }

    /**
     * Gets the localized data of the specified key
     * 
     * Note: This will toString() the provided enum.
     * 
     * @param key The key to use for lookup.
     * 
     * @return The image of the specified key	 
     */
    public Image getLocalizedData(String key) {

        Image image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(getLocalizedString(key)));
        }
        catch(Exception exception) {
            Tracelog.log(Level.SEVERE, false, exception);
        }

        return image;
    }
}