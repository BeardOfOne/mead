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

package engine.core.ui.navigation;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * A builder class for easily creating UI menus
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public final class MenuBuilder {

    /**
     * The host of this builder
     */
    private final JComponent _host;
    
	/**
	 * The root component of this menu system
	 */
	private JComponent _root;
	
	/**
	 * The list of components in chronologically added order
	 */
	private ArrayList<JComponent> _components = new ArrayList();
	
	/**
	 * Constructs a new instance of this class type
	 *
	 * @param host The host component of this menu
	 */
	private MenuBuilder(JComponent host) {
	    _host = host;
	}
	
	/**
	 * Adds an option builder to this option builder
	 * 
	 * @param builder The option builder to associate to this option builder's item
	 * 
	 * @return A reference to this option builder
	 */
	public MenuBuilder addBuilder(MenuBuilder builder) {
	    builder._root = _root;
	    _components.addAll(builder._components);
	    
	    return this;
	}

	/**
	 * Adds a menu with the specified text to the currently active menu
	 * 
	 * @param text The text to set as the menu title
	 * 
	 * @return A reference to the menu builder
	 */
	public MenuBuilder addMenu(String text) {
		AbstractMenuContainer component = new AbstractMenuContainer(_root == null ? _host : _root, text);
	
		if(_components.isEmpty() && _root == null) {
			_root = component.getComponent();
	    }
	    else {
	        _components.add(component.getComponent());    
	    }
	
		return this;
	}

	/**
	 * Adds a new item of the specified component to the builder
	 * 
	 * @param component The type of component to construct
	 * @param <T> A type extending an AbstractOption class
	 * 
	 * @return A reference to this builder
	 */
	public <T extends AbstractMenuItem> MenuBuilder addMenuItem(Class<T> component) {
		try {
		    T baseComponent = component.getConstructor(JComponent.class).newInstance(_root == null ? _host : _root);
		    if(_components.isEmpty() && _root == null) {
		        _root = baseComponent.getComponent();
		    }
		    else {
		        _components.add(baseComponent.getComponent());    
		    }
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}	
	
		return this;
	}

	/**
	 * Adds a separator to the list of options
	 * 
	 * @param <T> A type extending the class AbstractOption
	 * 
	 * @return A reference to this option builder
	 */
	public <T extends AbstractMenu> MenuBuilder addSeparator() {
		if(_root != null) {
			T component = (T) _root.getClientProperty(_root);
			component.addSeperator();			
		}
		
		return this;
	}

	/**
	 * Sets the root of this builder
	 * 
	 * @param root The root of this builder
	 * 
	 * @return A reference to this builder
	 */
	public MenuBuilder root(JComponent root) {
	    _root = root;
	    return this;
	}

	/**
	 * Resets the specified menu bar. The resetting of each portion of the menu is implementation
	 * specific per menu item for example, therefore the state of the said menu should be back
	 * in the 'original' starting state that it would normally be in
	 * 
	 * @param jMenuBar The menu bar to reset
	 */
	public static void reset(JMenuBar jMenuBar) {
		for(Component component : jMenuBar.getComponents()) {
			if(component instanceof JMenu) {
				JMenu menu = (JMenu)component;
				Object obj = menu.getClientProperty(menu);
				if(obj instanceof AbstractMenuContainer) {
					AbstractMenuContainer menuComponent = (AbstractMenuContainer) obj;
					menuComponent.reset();
				}
			}
		}
	}

	/**
	 * Builder entry-point
	 *  
	 * @param host The root component of this menu system
	 * 
	 * @return A reference to this builder
	 */
	public static MenuBuilder start(JComponent host) {
		return new MenuBuilder(host);
	}
	
	/**
	 * Builder entry-point
	 * 
     * @param host The root component of this menu system
     * 
     * @return A reference to this builder
	 */
	public static MenuBuilder start(MenuBuilder host) {
	    return start(host._root);
	}
}