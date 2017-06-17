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

package engine.core.option.types;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import engine.core.option.AbstractOption;

/**
 * Defines an abstract implementation for menu options, this is equivalent to a menu root
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public abstract class OptionMenu extends AbstractOption {

	/**
	 * Constructs a new instance of this type
	 * 
	 * @param component The component associated to this option menu
	 * @param parent The parent component associated to this option menu
	 */
	protected OptionMenu(JComponent component, JComponent parent) {
		super(component, parent);
	}
	
	@Override public final boolean enabled() {
		return super.enabled();
	}

	@Override public final boolean visibility() {
		return super.visibility();
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
						if(clientProperty instanceof OptionItem) {
						    OptionItem itemComponent = (OptionItem) jComponent.getClientProperty(jComponent);
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
