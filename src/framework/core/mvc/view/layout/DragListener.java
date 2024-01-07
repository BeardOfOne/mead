package framework.core.mvc.view.layout;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import framework.utils.MouseListenerEvent;

/**
 * Listener class used for providing draggable functionality to a component
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class DragListener extends MouseListenerEvent {

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
     * Stops the current drag event from occuring until the next drag event commences 
     */
    private boolean _stopDragging;
    
    /**
     * Constructs a new instance of this class type
     *
     * @param action The supported action
     */
    public DragListener(SupportedActions action) {
        super(action);
    }
    
    /**
     * Stops the current drag event from occuring
     */
    public void stopDragEvent() {
        _stopDragging = true;
    }
    
    /**
     *  @return TRUE if this drag listener is dragging, FALSE otherwise
     */
    public boolean isDragging() {
        return _dragging;
    }
    
    /**
     * Gets if the component was last dragged
     * 
     * @return TRUE if the component was dragged during its last operation, FALSE otherwise
     */
    public final boolean getLastDragged() {
        return _lastDragged;
    }
    
    @Override public void mouseReleased(MouseEvent event) {
            
        super.mouseReleased(event);
        if(event.isConsumed()) {
            return;
        }
        
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
        
        super.mousePressed(event);
        if(event.isConsumed() && getIsConsumed()) {
            return;
        }
        
        _stopDragging = false;
        _mousePressedEvent = event;
    }

    @Override public void mouseDragged(MouseEvent event) {
        super.mouseDragged(event);
        if(event.isConsumed() || getIsConsumed()) {
            return;
        }
        
        if(_stopDragging) {
            return;
        }
        
        if(_mousePressedEvent == null) {
            return;
        }
        
        // Get the component associated to the mouse event
        Component component = event.getComponent();
        
        // Store the location of the component
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