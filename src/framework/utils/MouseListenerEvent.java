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

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public class MouseListenerEvent extends MouseInputAdapter {

    private boolean _locked;
    
    private boolean validateMouseSupportedActions(MouseEvent event) {
        
        boolean result = true;
        if(!SwingUtilities.isLeftMouseButton(event)) {
            result = false;
        }
        
        return result;
    }
    
    @Override public void mousePressed(MouseEvent event) {
        if(_locked) {
            event.consume();
        }
        else if(validateMouseSupportedActions(event)) {
            _locked = true;
        }
        else {
            event.consume();
        }
    }

    @Override public void mouseDragged(MouseEvent event) {
        if(!_locked) {
            event.consume();
        }
    }
    
    @Override public void mouseReleased(MouseEvent event) {
        if(!_locked) {
            event.consume();
        }
        else if(validateMouseSupportedActions(event)) {
            _locked = false;
        }
        else {
            event.consume();
        }
    }
}
