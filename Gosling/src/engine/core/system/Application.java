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

package engine.core.system;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import engine.api.IDestructor;

/**
 * This class provides the entry point for all applications to extend
 * for their game.  Their main method should be within a class that extends
 * this class.
 * 
 * This class will construct the base listeners for the game, along with the engine
 * default values (pre-initialization) and other core functionality
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public abstract class Application extends JFrame implements IDestructor {

	/**
	 * Constructs a new instance of this class
	 */
    protected Application() {
        setListeners();
        setEngineDefaults();        
        setJMenuBar(new JMenuBar());
    }
    
    /**
     * Sets the listeners for the game
     */
    private void setListeners() {
        addComponentListener(new ComponentAdapter() {
            @Override public void componentHidden(ComponentEvent e) {
                setJMenuBar(null);
            }
            @Override public void componentShown(ComponentEvent e) {
                // Generate menu system
            	setWindowedInstanceMenu();
            	getJMenuBar().revalidate();
                getJMenuBar().repaint();
            }
        });
    }
    
    /**
     * Sets the menu functionality for the game
     */
    protected abstract void setWindowedInstanceMenu();
    
    /**
     * Sets the engine default values for the game
     * 
     * Note: This is a good place to initialize the resource manager
     * for localization.
     */
    protected abstract void setEngineDefaults();
}