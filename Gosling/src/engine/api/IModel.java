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

import java.util.HashMap;
import java.util.Map;

import engine.communication.internal.persistance.IXMLCodec;
import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.ISignalReceiver;

/**
 * This interface describes the general contract rules of all model type implementors 
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public interface IModel extends IDestructor, ISignalListener, IXMLCodec {
	
	/**
	 * This is the model properties class representation of a particular model
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 *
	 */
	public final class ModelProperties implements IDestructor {
				
		/**
		 * The mapping of signal names to signal implementations
		 */
		private final Map<String, ISignalReceiver> _signalListeners = new HashMap<>();
		
		/**
		 * Gets the list of signal listeners associated to the view
		 * 
		 * @return The list of signal listeners 
		 */
		public Map<String, ISignalReceiver> getSignalListeners() {
			return _signalListeners;
		}
		
		@Override public void dispose() {
			_signalListeners.clear();
		}

		@Override public void flush() {
			_signalListeners.clear();
		}	
	}
	
	/**
	 * Gets the model properties of the implementor of this interface
	 * 
	 * @return The model properties of the implementor
	 */
	public ModelProperties getModelProperties();
	
	@Override default Map<String, ISignalReceiver> getSignalListeners() {
		return getModelProperties().getSignalListeners();
	}
}