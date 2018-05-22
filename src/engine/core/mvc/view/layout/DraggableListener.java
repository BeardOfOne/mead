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

package engine.core.mvc.view.layout;

import java.awt.Component;
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

    /**
     * Indicates if the component is in a dragging state
     */
    private boolean _dragging;

    /**
     * Indicates if the last completed operation done by this component resulted in a drag
     */
    private boolean _lastDragged;
    
    /**
     * The owner associated to this 
     */
    private final Component _owner;

    /**
     * Constructs a new instance of this class type
     */
    public DraggableListener(Component component) {
        
        // Set the owner
        _owner = component;
        
        // Associated the proper listeners to what this listener class requires to operate 
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    /**
     * Gets if the component was last dragged
     * 
     * @return TRUE if the component was dragged during its last operation, FALSE otherwise
     */
    public final boolean getLastDragged() {
        return _lastDragged;
    }
    
    /**
     * @return The owner associated to this listener
     */
    public final Component getOwner() {
        return _owner;
    }

    @Override public void mouseReleased(MouseEvent event) {
        Object source = event.getSource();
        if(source instanceof Component) {
            Component component = (Component)source;
            component.revalidate();
        }

        // If the component was dragging then set the last dragged flag and reset
        // the dragging flag for the next run
        if(_dragging) {
            _dragging = false;
            _lastDragged = true;
        }
        else {
            _lastDragged = false;
        }
    }

    @Override public void mousePressed(MouseEvent event) {
        _mousePressedEvent = event;
    }

    @Override public void mouseDragged(MouseEvent event) {
        
        // Get the component associated to the mouse event
        Component component = event.getComponent();
        
        // Store the locatation of the component
        _location = component.getLocation(_location);

        // Set the location of the component
        component.setLocation(
                _location.x - _mousePressedEvent.getX() + event.getX(), 
                _location.y - _mousePressedEvent.getY() + event.getY()
                );

        // Indicate that the component has been dragged
        _dragging = true;
    }
}