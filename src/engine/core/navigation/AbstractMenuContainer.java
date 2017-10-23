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

package engine.core.navigation;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * Defines the abstract implementation for the menu container.  A menu container is similar 
 * in nature to a JMenu
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
class AbstractMenuContainer extends AbstractMenu {
	
	/**
	 * Constructs a new instance of this type
	 * 
	 * @param parent The parent component associated to this option menu
	 * @param text The text of the menu component that will be displayed 
	 */
	public AbstractMenuContainer(JComponent parent, String text) {
		super(new JMenu(text), parent);
	}
	
	/**
	 * Sets the mnemonic of this menu component
	 * 
	 * @param mnemonic The mnemonic to set to this menu component
	 */
	public void setMnemonic(int mnemonic) {
		super.get(JMenu.class).setMnemonic(mnemonic);
	}

	/**
	 * Resets this menu and its sub-items to their default states
	 */
	public final void reset() {
		for(Component component : super.get(JMenu.class).getMenuComponents()) {
			if(component instanceof JComponent) {
				JComponent jComponent = (JComponent) component;
				Object clientProperty = jComponent.getClientProperty(jComponent);
				if(clientProperty instanceof AbstractMenuItem) {
				    AbstractMenuItem menuItem = (AbstractMenuItem) jComponent.getClientProperty(jComponent);
				    menuItem.onReset();
				}
			}
		}
	}
	

	@Override public final void onExecute(ActionEvent actionEvent) {
	}
	

	@Override protected void onInitialize() {
		super.get(JMenu.class).addMenuListener(new MenuListener() {
			@Override public void menuSelected(MenuEvent e) {
				JMenu menu = (JMenu)e.getSource();
				for(Component component : menu.getMenuComponents()) {
					if(component instanceof JComponent) {
						JComponent jComponent = (JComponent) component;
						Object clientProperty = jComponent.getClientProperty(jComponent);
						if(clientProperty instanceof AbstractMenuItem) {
						    AbstractMenuItem itemComponent = (AbstractMenuItem) jComponent.getClientProperty(jComponent);
	                        jComponent.setEnabled(itemComponent.enabled());
	                        jComponent.setVisible(itemComponent.visibility());    
						}
					}
				}
			}
			@Override public void menuCanceled(MenuEvent e) {
			}
			@Override public void menuDeselected(MenuEvent e) {
			}			
		});
	}

}