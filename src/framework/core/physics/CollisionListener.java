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

import framework.api.IView;

/**
 * Handles collisions associated to a particular component within the same node level structure
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class CollisionListener extends MouseInputAdapter {

    /**
     * The source of where the collision took place
     */
    // TODO - This should be ICollide
    private final Component _source;
    
    /**
     * TRUE if the listener for the owner component is enabled, FALSE otherwise
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

        // Clears the collision
        _collision = null;
        
        // Populate all the siblings
        // TODO - should this be (ICollide)
        List<IView> siblings = new ArrayList();
        for(Component component : _source.getParent().getComponents()) {
            if(component instanceof IView) {
                siblings.add((IView)component);
            }
        }
    
        // Find all collided components 
        for(IView sibling : siblings) {
            if(sibling != _source && sibling instanceof ICollide && _source.getBounds().intersects(sibling.getContainerClass().getBounds())) {
                ICollide collidable = (ICollide)sibling;
                if(collidable.isValidCollision(_source)) {
                   _collision = collidable;
                   break;
                }
            }
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