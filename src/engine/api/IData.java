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

import java.awt.Image;
import java.util.List;
import java.util.UUID;

/**
 * Provides a data contract for all data type objects; data related objects
 * should drive your game, they put the 'data' in data-driven game development
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public interface IData {

	/**
	 * Gets the name of the IData implemented type
	 * 
	 * @return The name of the IData implemented type
	 */
	public String getName();
	
	/**
	 * Gets the friendly name of the data
	 * 
	 * @return The friendly name of the data 
	 */
	public String getFriendlyName();
	
	/**
	 * Gets the name of the layer associated to the data
	 * 
	 * @return The name, if any, of the layer.
	 */
	public List<UUID> getLayers();

	/**
	 * Gets the image data associated to the implemented data type
	 * 
	 * @return The image data
	 */
	public Image getImageData();
}