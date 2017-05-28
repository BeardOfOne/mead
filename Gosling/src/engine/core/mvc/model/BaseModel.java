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

import java.util.ArrayList;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import engine.api.IModel;
import engine.communication.internal.signal.IDataPipeline;
import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.ISignalReceiver;
import engine.communication.internal.signal.types.ModelEvent;
import engine.communication.internal.signal.types.PipelinedEvent;
import engine.communication.internal.signal.types.SignalEvent;

/**
 * A Game Model represents the base class of all model type objects
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseModel implements IModel 
{	
	/**
	 * The model properties for this model
	 */
	private transient final ModelProperties _modelProperties = new ModelProperties();
	
	/**
	 * Identifier for this model
	 */
	private final UUID _identifier = UUID.randomUUID();
			
	/**
	 * The list of receivers that can receive a message from the GameModel
	 */
	private transient final ArrayList<ISignalListener> _receivers = new ArrayList<>();

	/**
	 * The name of the operation to be performed
	 */
	private transient String _operationName;
	
	/**
	 * The event to submit when performing the request to do the operation
	 */
	private transient SignalEvent _operationEvent;
	
	// TODO - this should be deleted
	protected BaseModel() {
		registerSignalListeners();
	}
	
	/**
	 * The list of receivable objects that can receive messages 
	 * 
	 * @param receivers The list of receivers
	 */
	protected BaseModel(ISignalListener... receivers) {
		this();
		addListener(receivers);
	}
		
	/**
	 * Adds the specified listeners to listen in on signals fired by this model
	 * 
	 * @param receivers The list of receivers 
	 */
	public final void addListener(ISignalListener... receivers) {
		for(ISignalListener receiver : receivers) {
			if(!(receiver == null || _receivers.contains(receiver))) {
				_receivers.add(receiver);
			}			
		}
		
		// Set the event for listeners
		setOperation(EVENT_LISTENER_ADDED);
		
		// Push an update event to the listeners
		doneUpdating();
	}

	/**
	 * Removes a particular receiver from the list of listeners of this model
	 * 
	 * @param receiver The receiver to remove
	 */
	public final void removeListener(ISignalListener receiver) {
		_receivers.remove(receiver);
	}
	
	/**
	 * Refreshes this tile model, effectively doing a doneUpdate 
	 */
	public final void refresh() {
		
		// Create a new operation event to send out to listeners
		// In this case we specify a local event as not to disturb 
		// the done update functionality
		SignalEvent event = new ModelEvent(this, null);
		
		// Call all signal listeners with the specified event (this takes operation name into account)
		// and then it will end up calling update after the fact
		for(ISignalListener receiver : _receivers) {
			receiver.unicastSignalListener(event);
		}
	}

	/**
	 * A convenience method to indicate that an update has been performed
	 * and that this model should notify its receivers by issuing a signal
	 */
	protected final void doneUpdating() {
		
		// Create a new operation event to send out to listeners
		_operationEvent = new ModelEvent(this, _operationName);
		
		// Call all signal listeners with the specified event (this takes operation name into account)
		// and then it will end up calling update after the fact
		for(ISignalListener receiver : _receivers) {
			receiver.unicastSignalListener(_operationEvent);
		}

		// reset the contents created
		_operationName = null;
		_operationEvent = null;
	}
	
	
	/**
	 * Sets a particular operation name that will be converted into a signal and
	 * dispatched to all signal receivers of this model
	 * 
	 * @param operationName The name of the operation that is being performed
	 */
	protected final void setOperation(String operationName) {
		_operationName = operationName; 
	}
	
	/**
	 * Gets the current operation name if any
	 * 
	 * @return The name of the operation that is currently set
	 */
	protected final String getOperation() {
		return _operationName;
	}
	
	/**
	 * Gets the string representation of the unique identifier of this model
	 * 
	 * @return The UUID of this class
	 */
	@Override public final UUID getIdentifier() {
		return _identifier;
	}
	
	@Override public void registerSignalListeners() {
		registerSignalListener(ISignalListener.EVENT_REGISTER, new ISignalReceiver<SignalEvent>() {
			@Override public void signalReceived(SignalEvent event) {
				ISignalListener listener = (ISignalListener) event.getSource();
				addListener(listener);
			}
		});
		registerSignalListener(ISignalListener.EVENT_UNREGISTER, new ISignalReceiver<SignalEvent>() {
			@Override public void signalReceived(SignalEvent event) {
				ISignalListener listener = (ISignalListener) event.getSource();
				removeListener(listener);
			}
		});
		registerSignalListener(IModel.EVENT_PIPE_DATA, new ISignalReceiver<PipelinedEvent<IDataPipeline>>() {
			@Override public void signalReceived(PipelinedEvent<IDataPipeline> event) {				
				// Send a reference of the BaseModel back to the caller
				event.getSource().pipeData(BaseModel.this);
			}
		});
	}
	
	@Override public final ModelProperties getModelProperties() {
		return _modelProperties;
	}

	@Override public void flush() {
		_receivers.clear();
	}
	
	@Override public void dispose() {
		_receivers.clear();
		_modelProperties.dispose();
	}
	
	@Override public boolean equals(Object obj) {
		if(obj instanceof IModel) {
			IModel model = (IModel) obj;
			return model.getIdentifier().equals(this.getIdentifier());
		}
		
		return false;
	}
}