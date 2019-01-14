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

package framework.utils;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import framework.api.IView;

/**
 * This mouse listener natively support left-click and right-click concepts, and distinguishes from them
 * when actions are performed
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public class MouseListenerEvent extends MouseInputAdapter {

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
     * The action that this event listener supports
     */
    private final SupportedActions _action;
    
    /**
     * Constructs a new instance of this class type
     */
    public MouseListenerEvent() {
        // by default, left-click is supported only when nothing has been specified
        _action = SupportedActions.LEFT;
    }
    
    /**
     * Constructs a new instance of this class type
     *
     * @param action The action to have this event support
     */
    public MouseListenerEvent(SupportedActions action) {
        _action = action;
    }
    
    /**
     * Validates that the specified mouse event is a valid event
     *
     * @param event The mouse event that has occurred
     * 
     * @return TRUE if the mouse event can be operated on, FALSE otherwise
     */
    private boolean validateMouseSupportedActions(MouseEvent event) {
        
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
    
    @Override public void mousePressed(MouseEvent event) {

    	// If the specified event originated from an engine view type then
    	// verify that the mouse was properly over the card itself.
    	//
    	// This solves an issue where if you only support left-mouse and you right-mouse and hold and then left-mouse
    	// it would work. This makes it so that if you right-mouse and drag out, and then left-mouse, it will fail
    	// because the mousePressed did not originate within the bounds of the view container
    	if(event.getSource() instanceof IView) {
    		IView view = (IView)event.getSource();
    		if(!view.getContainerClass().getBounds().intersects(new Rectangle(new Dimension(event.getX(), event.getY())))) {
    			event.consume();
    			return;
    		}
    	}
    	
    	if(!validateMouseSupportedActions(event)) {
            event.consume();
        }
        else {
            _locked = true;
        }
    }

    @Override public void mouseDragged(MouseEvent event) {
        if(!_locked) {
            event.consume();
        }
    }
    
    @Override public void mouseReleased(MouseEvent event) {
        if(!_locked || !validateMouseSupportedActions(event)) {
            event.consume();
        }
        else { 
            _locked = false;
        }
    }
}
