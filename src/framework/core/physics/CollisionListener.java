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

package framework.core.physics;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import framework.utils.MouseListenerEvent;

/**
 * Handles collisions associated to a particular component within the same node level structure
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class CollisionListener extends MouseListenerEvent {
    
    /**
     * The object that has been collided with
     */
    private ICollidable _collision;
    
    /**
     * The source of the object
     */
    private final Component source;
    
    /**
     * Constructs a new instance of this class type
     *
     * @param source The source component to register collision events to
     * @param action The supported action
     */
    public CollisionListener(Component source, SupportedActions action) {
        super(action);
        this.source = source;
    }
    
    /**
     * @return The list of collided entities that the currently registered component has collided with
     */
    public ICollidable getCollision() {
        return _collision;
    }    
       
    @Override public void mousePressed(MouseEvent event) {
        super.mousePressed(event);
        if(event.isConsumed()) {
            return;
        }
        _collision = null;
    }
    
    @Override public void mouseReleased(MouseEvent event) {
        super.mouseReleased(event);
        if(event.isConsumed()) {
            return;
        }
        if(_collision != null) {
            _collision.onCollisionStop(source);
        }
    }
    
    @Override public void mouseDragged(MouseEvent event) {

        // Get the list of components that implement the ICollide type
        List<Component> siblings = new ArrayList();
        for(Component component : source.getParent().getComponents()) {
            if(component instanceof ICollidable) {
                siblings.add(component);
            }
        }

        // Find the first collided component
        boolean found = false;
        for(Component sibling : siblings) {
            if(sibling != source && sibling instanceof ICollidable && source.getBounds().intersects(sibling.getBounds())) {
                ICollidable collidable = (ICollidable)sibling;
                if(collidable.isValidCollision(source)) {
                    found = true;
                   _collision = collidable;
                   _collision.onCollisionStart(source);
                   break;
                }
            }
        }
        
        // If there was no collision that occured, ensure that the reference to any collision is no longer valid
        if(!found) {
            if(_collision != null) {
                _collision.onCollisionStop(source);
            }
            _collision = null;
        }
    } 
}