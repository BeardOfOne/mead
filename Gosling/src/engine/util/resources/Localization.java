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

package engine.util.resources;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import engine.api.IDestructor;

/**
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public final class Localization implements IDestructor {
	
	/**
	 * 
	 */
	private static Localization _instance;
	
	/**
	 * 
	 */
	private Locale _activeLocale;
	
	/**
	 * 
	 */
	private Map<Locale, ResourceBundle> _resources = new HashMap<>();
	
	/**
	 * 
	 */
	private Localization() {
	}
	
	/**
	 * 
	 * @return
	 */
	public static Localization instance()
	{
		if(_instance == null) {
			_instance = new Localization();
		}
		
		return _instance;
	}
	
	/**
	 * 
	 * @param bundle
	 */
	public void addLocale(ResourceBundle bundle) {
		_resources.put(bundle.getLocale(), bundle);
	}
	
	/**
	 * 
	 * @param bundle
	 * @param setActive
	 */
	public void addLocale(ResourceBundle bundle, boolean setActive) {
		addLocale(bundle);
		if(setActive) {
			setActiveLocale(bundle.getLocale());
		}
	}
	
	/**
	 * 
	 * @param locale
	 */
	public void setActiveLocale(Locale locale) {
		if(_resources.containsKey(locale)) {
			_activeLocale = locale;		
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public <T extends Object> T getResource(String key) { 
		return (T)_resources.get(_activeLocale).getObject(key);
	}

	@Override public void flush() {
		_resources.clear();
		_activeLocale = null;
	}

	@Override public void dispose() {
		_instance = null;
		try {
			finalize();
		} 
		catch (Throwable exception) {
			exception.printStackTrace();
		}
	}
}