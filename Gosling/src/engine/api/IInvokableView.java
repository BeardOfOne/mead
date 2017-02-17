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

package engine.api;

import javax.swing.JComponent;

/**
 * This interfaces specifies that an implementing object can be invoked by an outside source.
 * This entity will be capable of being invoked and will have the notion of being able to return
 * some result back to whomever invokes this object
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public interface IInvokableView<T extends Enum<T>, U extends JComponent> extends IDestructor {
	/**
	 * Invokes the implemented object, similar to a lazy-load operation in many ways
	 * Note: Calling this more than one could have unintended side-effects as I have not
	 * coded against this yet.
	 */
	public void invoke();
	
	/**
	 * Gets the result of the specified key.  This key should be an enumeration of some sort
	 * implemented by the programmer
	 * 
	 * @param key The key to lookup
	 * 
	 * @return The value of the lookup
	 */
	public U getResult(T key);	
}