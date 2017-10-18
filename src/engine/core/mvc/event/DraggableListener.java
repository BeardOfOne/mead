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
package engine.core.mvc.event;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

/**
 * Listener class used for providing draggable functionality to a component
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class DraggableListener extends MouseInputAdapter {
	
	/**
	 * The position of the mouse
	 */
    private Point _location;
    
    /**
     * The event that occurred as a result of the mouse being pressed down
     */
    private MouseEvent _mousePressedEvent;
 
    @Override public void mouseReleased(MouseEvent event) {
    		Object source = event.getSource();
    		if(source instanceof Component) {
    			Component component = (Component)source;
    			component.revalidate();
    		}
    }
    
    @Override public void mousePressed(MouseEvent event) {
    		_mousePressedEvent = event;
    }
    
    @Override public void mouseEntered(MouseEvent event) {
    		event.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    @Override public void mouseExited(MouseEvent event) {
		event.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    @Override public void mouseDragged(MouseEvent event) {
        Component component = event.getComponent();
        _location = component.getLocation(_location);
        
        component.setLocation(
        		_location.x - _mousePressedEvent.getX() + event.getX(), 
        		_location.y - _mousePressedEvent.getY() + event.getY()
    		);
     }
}