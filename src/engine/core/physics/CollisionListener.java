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

public final class CollisionListener extends MouseInputAdapter {
    
    private final List<Component> _collisions = new ArrayList();
    
    public CollisionListener(Component component){
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }
    
    @Override public void mouseDragged(MouseEvent event) {
        
        _collisions.clear();
        
        Component component = event.getComponent();
        Component[] siblings = component.getParent().getComponents();
    
        for(Component sibling : siblings) {
            if(sibling != component && sibling.getClass().equals(component.getClass())) {
                if(isCollided(component, sibling)) {
                    _collisions.add(sibling);
                }                   
            }
        }
    }
    
    public List<Component> getCollisions() {
        return new ArrayList(_collisions);
    }
    
    private boolean isCollided(Component source, Component destination) {
        return false;
    }
}