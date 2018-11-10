/**
 * Daniel Ricci <thedanny09@icloud.com>
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
package framework.communication.internal.signal.arguments;

import java.util.EventObject;

/**
 * Base class for all event arguments
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class EventArgs extends EventObject {

    /**
     * Indicates if this event should not be carried forward
     */
    private boolean _suppressUpdate;

    
    private boolean _setDestinationAsTarget;
    
    /**
     * The name of the operation 
     */
    private final String _operationName;

    /**
     * Constructs a new instance of this class type
     * 
     * @param sender The sender
     * @param operationName The name of the operation
     */
    public EventArgs(Object sender, String operationName) {
        super(sender);
        _operationName = operationName;
    }
    
    public void setSender(Object sender) {
        source = sender;
    }
    
    /**
     * Sets the suppress update state
     *
     * @param isSuppressed If this event should be suppressed during any update phases
     */
    public void setSuppressUpdate(boolean isSuppressed) {
        _suppressUpdate = isSuppressed;
    }
    
    /**
     * @return Gets the suppress update state of this event
     */
    public boolean isUpdateSuppressed() {
        return _suppressUpdate;
    }
    
    public void setDestinationAsTarget(boolean enabled) {
        _setDestinationAsTarget = enabled;
    }
    
    public boolean isDestinationUsedAsTarget() {
        return _setDestinationAsTarget;
    }
    
    /**
     * Gets the operation name
     * 
     * @return The name of the operation
     */
    public final String getOperationName() {
        return _operationName;
    }
    
    public static EventArgs Empty() {
        return new EmptyEventArgs();
    }
    
    @Override public final Object getSource() {
        return super.getSource();
    }
}
