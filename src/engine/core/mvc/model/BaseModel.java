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

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import engine.api.IModel;
import engine.communication.internal.signal.IDataPipeline;
import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.ISignalReceiver;
import engine.communication.internal.signal.arguments.AbstractEventArgs;
import engine.communication.internal.signal.arguments.ModelEventArgs;
import engine.communication.internal.signal.arguments.PipelinedEventArgs;
import engine.core.graphics.IRenderable;
import engine.core.mvc.common.CommonProperties;

/**
 * The base model representation of all models in the application
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseModel implements IModel, IRenderable 
{
    public static final String MODEL_REFRESH = "MODEL_REFRESH";

    /**
     * The universally unique identifier associated to this model
     */
    @XmlAttribute(name="UUID")
    private UUID _uuid = UUID.randomUUID();

    /**
     * The model properties for this model
     */
    private transient final CommonProperties _modelProperties = new CommonProperties();

    /**
     * The list of listeners that can receive a message from the model
     */
    private transient final List<ISignalListener> _listeners = new ArrayList();

    /**
     * The name of the operation to be performed
     */
    private transient String _operationName;

    /**
     * The event to submit when performing the request to do the operation
     */
    private transient AbstractEventArgs _operationEvent;

    /**
     * Indicates if this model should suppress updates to its listeners
     */
    private transient boolean _suppressUpdates;

    /**
     * Constructs a new instance of this class type
     */
    protected BaseModel() {
        registerSignalListeners();
    }

    /** 
     * Constructs a new instance of this class type
     *
     * @param listeners The list of receivers
     */
    protected BaseModel(ISignalListener... listeners) {
        this();
        addListenersImpl(listeners);
    }

    /**
     * Adds the specified listeners to listen in on signals fired by this model
     * 
     * @param listeners The list of listeners 
     */
    @Override public final void addListeners(ISignalListener... listeners) {
        addListenersImpl(listeners);

        // Set the event for listeners
        setOperation(EVENT_LISTENER_ADDED);

        // Push an update event to the listeners
        doneUpdating();
    }

    /**
     * Adds the specified listeners to this model
     * 
     * @param listeners The listeners to add to this model
     */
    private void addListenersImpl(ISignalListener... listeners) {
        for(ISignalListener listener : listeners) {
            if(!(listener == null || _listeners.contains(listener))) {
                _listeners.add(listener);
            }
        }
    }

    /**
     * Gets the specified listener based on its class type
     * 
     * @param classType The class type of the listener
     * @param <T> A type extending The class template type
     * 
     * @return The listener associated to this class type
     */
    public final <T extends ISignalListener> T getListener(Class<T> classType) {
        for(ISignalListener listener : _listeners) {
            if(listener.getClass().equals(classType)) {
                return (T) listener;
            }
        }

        return null;
    }
    
    public int getListenerCount() {
        return _listeners.size();
    }

    /**
     * Gets if the specified listener is listening in on this model for messages
     * 
     * @param listener The listener
     * @param <T> ISignalListener derived type
     * 
     * @return TRUE if this model is sending messages to the specified listener, FALSE otherwise
     */
    public final <T extends ISignalListener> boolean isModelListening(T listener) {
        if(listener != null)  {
            for(ISignalListener receiver : _listeners) {
                if(receiver.equals(listener)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes a particular receiver from the list of listeners of this model
     * 
     * @param receiver The receiver to remove
     */
    public final void removeListener(ISignalListener receiver) {
        _listeners.remove(receiver);
    }

    /**
     * Refreshes this tile model, effectively doing a doneUpdate however 
     * a MODEL_REFRESH tag will be used as the command type 
     */
    @Override public void refresh() {
        refresh(MODEL_REFRESH);
    }
    
    @Override public void refresh(String operationName) {
        // Do not continue with the update if there is a suppression
        // of the updates
        if(isSuppressingUpdates()) {
            return;
        }

        // Create a new operation event to send out to listeners
        // In this case we specify a local event as not to disturb 
        // the done update functionality
        AbstractEventArgs event = new ModelEventArgs(this, operationName);

        // Call all signal listeners with the specified event (this takes operation name into account)
        // and then it will end up calling update after the fact
        for(ISignalListener receiver : _listeners) {
            receiver.sendSignalEvent(event);
        }
    }

    /**
     * Sets if this model should suppress updates
     * 
     * @param suppressUpdates The state of the suppress update flag
     */
    public final void setSuppressUpdates(boolean suppressUpdates) {
        _suppressUpdates = suppressUpdates;
    }

    /**
     * Indicates if this model is suppressing updates
     * 
     * @return TRUE if this model is suppressing updates
     */
    public final boolean isSuppressingUpdates() {
        return _suppressUpdates;
    }

    /**
     * A convenience method to indicate that an update has been performed
     * and that this model should notify its receivers by issuing a signal
     */
    public final void doneUpdating() {

        // Do not continue with the update if there is a suppression
        // of the updates
        if(isSuppressingUpdates()) {
            return;
        }

        // Create a new operation event to send out to listeners
        _operationEvent = new ModelEventArgs(this, _operationName);

        // Call all signal listeners with the specified event (this takes operation name into account)
        // and then it will end up calling update after the fact
        for(ISignalListener receiver : _listeners) {
            receiver.sendSignalEvent(_operationEvent);
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

    @Override public final UUID getUUID() {
        return _uuid;
    }

    @Override public void registerSignalListeners() {
        addSignalListener(ISignalListener.EVENT_REGISTER, new ISignalReceiver<AbstractEventArgs>() {
            @Override public void signalReceived(AbstractEventArgs event) {
                ISignalListener listener = (ISignalListener) event.getSource();
                addListeners(listener);
            }
        });
        addSignalListener(ISignalListener.EVENT_UNREGISTER, new ISignalReceiver<AbstractEventArgs>() {
            @Override public void signalReceived(AbstractEventArgs event) {
                ISignalListener listener = (ISignalListener) event.getSource();
                removeListener(listener);
            }
        });
        addSignalListener(IModel.EVENT_PIPE_DATA, new ISignalReceiver<PipelinedEventArgs>() {
            @Override public void signalReceived(PipelinedEventArgs event) {
                IDataPipeline source = (IDataPipeline) event.getSource();
                source.pipeData(BaseModel.this);
            }
        });
    }

    @Override public void copyData(IModel model) {
        _uuid = model.getUUID();
    }

    @Override public final CommonProperties getModelProperties() {
        return _modelProperties;
    }

    @Override public void clear() {
        _listeners.clear();
        _modelProperties.clear();
    }

    @Override public boolean equals(Object obj) {
        if(obj instanceof IModel) {
            IModel model = (IModel) obj;
            return model.getUUID().equals(this.getUUID());
        }

        return false;
    }
    
    @Override public void update(AbstractEventArgs signalEvent) {
    }
    
    @Override public Image getRenderableContent() {
        return null;
    }
}