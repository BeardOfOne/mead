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

package engine.managers;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Defines a class in charge of managing localization across an application
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public final class LocalizationManager {
	
	/**
	 * The single instance
	 */
	private static LocalizationManager _instance;
	
	/**
	 * The locale being used
	 */
	private Locale _locale = Locale.CANADA;
	
	/**
	 * Resources used to define what can be fetched from the properties
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 *
	 */
	public enum Resources
	{
		ChessTitle,
		ChessTitleDeveloper,
		FileMenu,
		ExitMenu,
		HelpMenu,
		AboutMenu,
		NewGame, 
		ResetPosition,
		HighlightNeighbors;
	}
	
	/**
	 * Constructs an object of this class
	 * 
	 */
	private LocalizationManager()
	{
	}
	
	/**
	 * Gets the singleton instance
	 * 
	 * @return The singleton instance
	 */
	private static LocalizationManager Instance()
	{
		if(_instance == null)
		{
			_instance = new LocalizationManager();
		}
		
		return _instance;
	}
	
	/**
	 * Gets the specified resource
	 * 
	 * @param resource The resource to get
	 * 
	 * @return The value of the resource specified
	 */
	public static String Get(Resources resource)
	{
		ResourceBundle messages = ResourceBundle.getBundle("internal.resources", Instance()._locale);
		return messages.getString(resource.toString().toLowerCase());			
	}
}
