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

package engine.core.physics;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MouseInputAdapter;

/**
 * Handles collisions associated to a particular component within the same node level structure
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class CollisionListener extends MouseInputAdapter {
    
    /**
     * The list of collided components
     */
    private final List<Component> _collisions = new ArrayList();
    
    /**
     * Constructs a new instance of this class type
     *
     * @param component The component to register collision events to
     */
    public CollisionListener(Component component){
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }
    
    @Override public void mouseDragged(MouseEvent event) {
        
        // Clears the collisions
        _collisions.clear();
        
        final Component thisComponent = event.getComponent();
        final Component[] thisComponentsSiblings = thisComponent.getParent().getComponents();
    
        // Find all collided components 
        for(Component sibling : thisComponentsSiblings) {
            if(sibling != thisComponent && sibling.getClass().equals(thisComponent.getClass()) && thisComponent.getBounds().intersects(sibling.getBounds())) {
                _collisions.add(sibling);
            }
        }
    }
    
    /**
     * @return The list of collided entities that the currently registered component has colided with
     */
    public List<Component> getCollisions() {
        return new ArrayList(_collisions);
    }    
}