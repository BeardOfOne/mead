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

/**
 * Specifies a contract for flushing (clearing) resources effectively resetting its state, and
 * disposing of an object properly.
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public interface IDestructor {

	/**
	 * If you want to reset this object than call this method.  If you 
	 * want to destroy the object then you are better of calling Dispose
	 * 
	 * Sets all fields of this object to its default set of values
	 * effectively reseting the current object.  
	 * 
	 */
	public void flush();
	
	/**
	 * Call this method if you want to ready your object for finalize without
	 * having to do anything special with it before-hand
	 * 
	 * Sets all fields of this object to its default set of values
	 * effectively reseting the current object.  
	 * 
	 * This method should release all handles  
	 * 
	 * This method should bubble up and affect the entire hierarchy from this object
	 * onwards
	 */
	public void dispose();
}