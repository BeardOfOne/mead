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

package engine.utils.globalisation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import engine.api.IDestructor;

/**
 * Defines the functionality for providing localization in an application 
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public abstract class Localization<T extends Enum<T>> implements IDestructor {
	
	/**
	 * The active local of this localization effort
	 */
	private Locale _activeLocale;
	
	/**
	 * A mapping of resources of locales to resource bundles
	 */
	private final Map<Locale, ResourceBundle> _resources = new HashMap<>();
	
	/**
	 * Adds the specified locale to this localization effort
	 * 
	 * @param bundle The resource bundle to add to this localization effort
	 */
	private final void addLocale(ResourceBundle bundle) {
		_resources.put(bundle.getLocale(), bundle);
	}
	
	/**
	 * Adds the specified bundle to the localization and indicates if it is active
	 *  
	 * @param bundle The bundle to add to the locale
	 * @param setActive If the bundle is active
	 */
	public final void addLocale(ResourceBundle bundle, boolean setActive) {
		addLocale(bundle);
		if(setActive) {
			setActiveLocale(bundle.getLocale());
		}
	}
	
	/**
	 * Sets the active locale of this localization effort
	 * 
	 * @param locale The locale to set
	 */
	private final void setActiveLocale(Locale locale) {
		if(_resources.containsKey(locale)) {
			_activeLocale = locale;		
		}
	}
	
	/**
	 * Gets a localized string of the specified key
	 * 
	 * @param key The key to lookup
	 * 
	 * @return The string associated to the key
	 */
	public final String getLocalizedString(T key) {
		return getResource(key.toString());
	}
	
	/**
	 * Gets a resource associated to the specified key
	 * 
	 * @param key The key resource to get
	 * @param <U> A type extending the class {@link Object}
	 * 
	 * @return The object associated to the resource
	 */
	private final <U extends Object> U getResource(String key) { 
		return (U)_resources.get(_activeLocale).getObject(key);
	}

	@Override public boolean flush() {
		_resources.clear();
		_activeLocale = null;
		return true;
	}
}