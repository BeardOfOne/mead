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

package engine.communication.external.builder;

import engine.communication.external.filesystem.AbstractFileSystem;

/**
 * The abstract representation of the methods implemented by all builder types
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public abstract class AbstractBuilder<T extends AbstractFileSystem>  {
		
	/**
	 * File system used by the builder
	 */
	protected T _fileSystem;
	
	/**
	 * Performs all user-input related tasks before going through the 
	 * list of automated build processes
	 * 
	 * @return The success of the user-input to determine if the build process should continue
	 */
	public abstract boolean buildStart();
	
	/**
	 * Builds custom content
	 */
	public abstract void buildContent();
	
	/**
	 * Finalizes the building of the builder's tasks
	 */
	public abstract void buildEnd();
}