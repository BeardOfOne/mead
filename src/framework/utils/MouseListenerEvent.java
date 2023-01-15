/**
* Daniel Ricci {@literal <thedanny09@icloud.com>}
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

package framework.utils;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

/**
 * This mouse listener natively support left-click and right-click concepts, and distinguishes from them
 * when actions are performed
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public class MouseListenerEvent extends MouseInputAdapter {

    /**
     * Flag indicating if the last processed event is in a consumed state
     */
    private boolean _isConsumed;
    
    /**
     * A flag indicating if this listener is enabled
     */
    private boolean _isEnabled = true;
    
    /**
     * The set of supported actions available for this event to support
     * 
     * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
     *
     */
    public enum SupportedActions { LEFT, RIGHT };
    
    /**
     * A flag indicating that this mouse listener is in a locked state
     */
    private boolean _locked;
    
    /**
     * A flag indicating that this mouse listener when through the mouse entered state
     */
    private boolean _mouseEntered;
    
    /**
     * The action that this event listener supports
     */
    private final SupportedActions _action;
    
    /**
     * Constructs a new instance of this class type
     * 
     * @param action the supported action
     */
    public MouseListenerEvent(SupportedActions action) {
        _action = action;
    }
    
    /**
     * Sets if this listener is enabled
     *
     * @param isEnabled TRUE if this listener is enabled, FALSE otherwise
     */
    public final void setEnabled(boolean isEnabled) {
        _isEnabled = isEnabled;
    }
    
    /**
     * Validates that the specified mouse event is a valid event
     *
     * @param event The mouse event that has occurred
     * 
     * @return TRUE if the mouse event can be operated on, FALSE otherwise
     */
    private boolean processMouseEvent(MouseEvent event) {
        
        boolean result;
        
        switch(_action) {
        case LEFT:
            result = (event.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK;
            break;
        case RIGHT:
            result = (event.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK;
            break;
        default:
            result = false;
            break;
        }
        
        return result;
    }
    
    public final boolean getIsConsumed() {
        return _isConsumed;
    }
    
    /**
     * @return TRUE if this listener is enabled, FALSE otherwise
     */
    public final boolean getIsEnabled() {
        return _isEnabled;
    }
    
    @Override public void mouseEntered(MouseEvent event) {
        _mouseEntered = true;
    }
    
    @Override public void mouseExited(MouseEvent event) {
        _mouseEntered = false;
    }
    
    @Override public void mousePressed(MouseEvent event) {
        _isConsumed = false;
        if(!_isEnabled || !_mouseEntered || !processMouseEvent(event)) {
            event.consume();
            _isConsumed = true;
        }
        else {
            _locked = true;
        }
    }

    @Override public void mouseDragged(MouseEvent event) {
        if(event.isConsumed()) {
            return;
        }
        
        _isConsumed = false;
        if(!_isEnabled || !_locked) {
            event.consume();
            _isConsumed = true;
        }
    }
    
    @Override public void mouseReleased(MouseEvent event) {
        _isConsumed = false;
        if(!_isEnabled || !_locked || !processMouseEvent(event)) {
            event.consume();
            _isConsumed = true;
        }
        else { 
            _locked = false;
        }
    }
}
