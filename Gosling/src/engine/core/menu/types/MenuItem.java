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

package engine.core.menu.types;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import engine.core.menu.AbstractMenu;

/**
 * Defines an abstract class for option items, these are equivalent to menu items
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public abstract class MenuItem extends AbstractMenu {

	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param component The component to associate to this option item
	 * @param parent The parent component associated to this option item
	 */
	protected MenuItem(JComponent component, JComponent parent) {
		super(component, parent);
	}	
	
	@Override protected final void onInitialize() {
		super.get(JMenuItem.class).addActionListener(new AbstractAction(super.toString()) {
			@Override public void actionPerformed(ActionEvent actionEvent) {
				onExecute(actionEvent);
			}
		});
	}	
	
	@Override protected void onReset() {
	}
}