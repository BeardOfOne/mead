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

package engine.communication.internal.signal.arguments;

import java.util.EventObject;

import engine.communication.internal.signal.ISignalReceiver;

/**
 * The top-level class to form messages when invoking a signal
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class SignalEventArgs<T extends Object> extends EventObject {
	
	/**
	 * The name of the operation which is used to identify
	 * which {@link ISignalReceiver } to invoke
	 */
	private String _operationName;
	
	private SignalEventArgs(T sender) {
		super(sender);
	}
	
	/**
	 * Constructs a new signal type event
	 * 
	 * @param sender The sender source
	 * @param operationName The name of the operation being performed
	 */
	public SignalEventArgs(T sender, String operationName) {
		this(sender);	
		_operationName = operationName;
	}
	
	/**
	 * Verifies the validity of the signal event.  Validity is determined
	 * by the expected usage of SignalEvent reference in a general case.
	 * 
	 * @return The validity of this SignalEvent
	 */
	public final boolean isValid() {
		return !(this instanceof NullEventArgs || _operationName == null || _operationName.trim().isEmpty());
	}
	
	/**
	 * Gets the operation name
	 * 
	 * @return The name of the operation
	 */
	public final String getOperationName() {
		return _operationName;
	}	
	
	/**
	 * Gets the source with respect to the specified type
	 */
	@Override public final T getSource() {
		return (T)super.getSource();
	}
}