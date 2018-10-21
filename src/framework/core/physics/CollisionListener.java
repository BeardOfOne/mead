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

package framework.core.physics;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MouseInputAdapter;

/**
 * Handles collisions associated to a particular component within the same node level structure
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class CollisionListener extends MouseInputAdapter {

    /**
     * Indicates if this collision listener can only ever collide with at-most one element at a time
     */
    private boolean _isCollisionSingular;
    
    /**
     * The source of where the collision took place
     */
    // TODO - This should be ICollide
    private final Component _source;
    
    /**
     * TRUE if the listener for the owner component is enabled
     */
    private boolean _isEnabled;
    
    /**
     * The object that has been collided with
     */
    private ICollide _collision;
    
    /**
     * Constructs a new instance of this class type
     *
     * @param source The source component to register collision events to
     */
    public CollisionListener(Component source) {
        _source = source;
        setEnabled(true);
    }
       
    @Override public void mousePressed(MouseEvent event) {
        // When the mouse button has been pressed down on this listener, the collision should be cleared
        // or else a false-positive could end up happening
        _collision = null;
    }
    
    @Override public void mouseDragged(MouseEvent event) {

        // Before going through the list of collided components, verify if what was last colided (if any)
        // is still being collided.
        if(_isCollisionSingular && _collision != null) {
            if(_collision.isValidCollision(_source)) {
                return;  
            }
        }

        // Get the list of components that implement the ICollide type
        List<Component> siblings = new ArrayList();
        for(Component component : _source.getParent().getComponents()) {
            if(component instanceof ICollide) {
                siblings.add(component);
            }
        }

        // Find the first collided component
        boolean found = false;
        for(Component sibling : siblings) {
            if(sibling != _source && sibling instanceof ICollide && _source.getBounds().intersects(sibling.getBounds())) {
                ICollide collidable = (ICollide)sibling;
                if(collidable.isValidCollision(_source)) {
                    found = true;
                   _collision = collidable;
                   break;
                }
            }
        }
        
        // If there was no collision that occured, ensure that the reference to any collision is no longer valid
        if(!found) {
            _collision = null;
        }
    }
    
    /**
     * Sets if this listener is enabled
     *
     * @param isEnabled TRUE if this listener is enabled, FALSE otherwise
     */
    public void setEnabled(boolean isEnabled) {
        if(_isEnabled == isEnabled) {
            return;
        }
        
        _isEnabled = isEnabled;
        
        if(!isEnabled) {
            _source.removeMouseListener(this);
            _source.removeMouseMotionListener(this);
        }
        else {
            _source.addMouseListener(this);
            _source.addMouseMotionListener(this);
        }
    }
    
    /**
     * Sets if this collision listener can only ever collide with at most one object. When a
     * valid collision has been detected, that collided object will take precedence over any other
     * object until collision with that object is no longer valid
     *
     * @param isEnabled If this option is enabled
     */
    public void setIsSingularCollision(boolean isSingularCollision) {
        _isCollisionSingular = isSingularCollision;
    }
    
    /**
     * Gets if this listener is enabled
     *
     * @return TRUE if this listener is enabled, FALSE otherwise
     */
    public boolean getIsEnabled() {
        return _isEnabled;
    }
    
    /**
     * @return The list of collided entities that the currently registered component has collided with
     */
    public ICollide getCollision() {
        return _collision;
    }    
}