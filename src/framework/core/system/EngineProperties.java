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

/**
 * The engine properties associated to the engine at runtime
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class EngineProperties {

    /**
     * The mapping of local properties 
     */
    private final Map<Property, String> _engineProperties = new HashMap();

    /**
     * The singleton instance of this class
     */
    private static EngineProperties _instance;

    /**
     * Enum values for properties in the engine properties
     * 
     * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
     *
     */
    public enum Property {

        /**
         * The path where the .cvs (comma-separated-value) is stored for localization management
         */
        LOCALIZATION_PATH_CVS,

        /**
         * The path where the .xml is stored for marshalling/unmarshalling 
         */
        DATA_PATH_XML,

        /**
         * The path where the sheet is stored, this is all the images (tilemap or atlas, etc)
         */
        DATA_PATH_SHEET,

        /**
         * The path where the logging will be directed
         */
        LOG_DIRECTORY,

        /**
         * If the engine logging should be omitted from the standard output stream of the console
         */
        ENGINE_OUTPUT,
        
        /**
         * If the engine should supress the display signal registration
         */
        SUPPRESS_SIGNAL_REGISTRATION_OUTPUT,
        
        /**
         * Disables translations placeholder, this will show the text entered as-written
         */
        DISABLE_TRANSLATIONS_PLACEHOLDER,
        
        /**
         * Flag indicating if exceptions that are thrown through the logging system should be displayed as an alert to the user
         */
        DISPLAY_EXCEPTIONS
    }

    /**
     * Constructs a new instance of this class type
     */
    private EngineProperties() {
    }

    /**
     * Gets the singleton instance of this class type
     * 
     * @return The singleton instance of this class type
     */
    public static final EngineProperties instance() {
        if(_instance == null) {
            _instance = new EngineProperties();
        }

        return _instance;
    }

    /**
     * Sets the specified property inside of the engine
     * 
     * @param property The property to append into the engine properties
     * @param value The value associated to the property being appended
     */
    public void setProperty(Property property, String value) {
        _engineProperties.put(property, value);
    }

    /**
     * Gets the value of the specified property
     * 
     * @param property The property to fetch for its value
     * 
     * @return The value associated to the specified property
     */
    public String getProperty(Property property) {
        return _engineProperties.get(property);
    }

    /**
     * Indicates if the set property is boolean valid
     *
     * @param property The property
     * @return TRUE if the property toString() value is truthy, FALSE otherwise
     */
    public boolean getIsPropertyValid(Property property) {
        return Boolean.parseBoolean(getProperty(property));
    }

    /**
     * Indicates if the set property is boolean invalid
     *
     * @param property The property
     * @return TRUE if the property toString() value is not truthy, FALSE otherwise
     */
    public boolean getIsPropertyInvalid(Property property) {
        return !getIsPropertyValid(property);
    }
    
    /**
     * Gets if the engine properties has enough information to load data for the current session
     * 
     * @return TRUE if the engine properties holds property values for the data paths, FALSE otherwise
     */
    public boolean hasDataValues() {
        return _engineProperties.containsKey(Property.DATA_PATH_SHEET) || _engineProperties.containsKey(Property.DATA_PATH_XML);
    }
}