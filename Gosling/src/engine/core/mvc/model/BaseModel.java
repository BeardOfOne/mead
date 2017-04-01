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

package engine.core.mvc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import engine.api.IModel;
import engine.communication.internal.signal.ISignalReceiver;
import engine.communication.internal.signal.types.ModelEvent;
import engine.communication.internal.signal.types.SignalEvent;

/**
 * A Game Model represents the base class of all model type objects
 */
public abstract class BaseModel implements IModel, Serializable
{
	/**
	 * Identifier for this model
	 */
	private UUID _identifier = UUID.randomUUID();
			
	/**
	 * The list of receivers that can receive a message from the GameModel
	 */
	private final ArrayList<ISignalReceiver> _receivers = new ArrayList<>();

	/**
	 * The name of the operation to be performed
	 */
	private String _operationName;
	
	/**
	 * The event to submit when performing the request to do the operation
	 */
	private SignalEvent _operationEvent;
	
	/**
	 * The list of receivable objects that can receive messages 
	 * 
	 * @param receivers The list of receivers
	 */
	protected BaseModel(ISignalReceiver... receivers) {
		for(ISignalReceiver receiver : receivers) {
			addReceiver(receiver);
		}
	}
		
	public final void addReceiver(ISignalReceiver receiver) {
		if(!(receiver == null || _receivers.contains(receiver))) {
			_receivers.add(receiver);
		}
	}

	public final void removeReciever(ISignalReceiver receiver) {
		_receivers.remove(receiver);
	}

	protected final void doneUpdating() {
		
		_operationEvent = new ModelEvent(this, _operationName);
		
		for(ISignalReceiver receiver : _receivers) {
			receiver.sendSignal(_operationEvent);
		}		
		
		_operationName = null;
		_operationEvent = null;
	}	
	
	protected final void setOperation(String operationName) {
		_operationName = operationName; 
	}
	
	protected final String getOperation() {
		return _operationName;
	}
	
	public final String getIdentifier() {
		return _identifier.toString();
	}
	
	@Override public void dispose() {
		_identifier = null;
		_receivers.clear();
	}
	
	@Override public final boolean equals(Object obj) {
		if(obj instanceof BaseModel) {
			BaseModel model = (BaseModel) obj;
			return model.getIdentifier() == model.getIdentifier();
		}
		
		return false;
	}

	@Override public void flush() {		
	}
}