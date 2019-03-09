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

package framework.core.navigation;

import java.util.UUID;

import javax.swing.JComponent;
import javax.swing.JSeparator;

/**
 * Defines the abstract implementation for the menu.
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
abstract class AbstractMenu {

    /**
     * The unique identifier of the menu item
     */
    private final UUID _identifier = UUID.randomUUID();

    /**
     * The parent key associated to this option
     */
    private final String _parentKey = "__parent__";

    /**
     * The component associated to this option
     */
    private final JComponent _component;

    /**
     * The parent component associated to this option
     */
    private final JComponent _parent;

    /**
     * Constructs a new instance of this class type
     * 
     * @param component The component to associate to this option
     * @param parent The parent component associated to this option
     */
    protected AbstractMenu(JComponent component, JComponent parent) {

        _component = component;
        _parent = parent;

        _component.putClientProperty(component, this);

        if(parent != null) {
            if(_parent.getClientProperty(_parentKey) == null) {
                _parent.putClientProperty(_parentKey, parent);
            }
            _parent.add(_component);
        }

        onInitialize();
    }

    /**
     * Constructs a new instance of this class type
     * 
     * @param component The component to associate to this option
     * @param parent The abstract option to associate to this option
     */
    protected AbstractMenu(JComponent component, AbstractMenu parent) {
        this(component, parent._component);
    }

    /**
     * Adds a separator to this option
     */
    protected void addSeperator() {
        JSeparator separator = new JSeparator();
        _component.add(separator);
    }

    /**
     * Gets if the option is enabled
     * 
     * @return TRUE if the option is enabled, FALSE if the option is not enabled
     */
    protected boolean isEnabled() {
        return _component.isEnabled();
    }

    /**
     * Gets the specified component type
     * 
     * @param componentClass The class of the component to get
     * @param <T> A type extending a JComponent class
     * 
     * @return The specified component class
     */
    public <T extends JComponent> T getComponent(Class<T> componentClass) {
        return (T)_component;
    }

    /**
     * Gets the component associated to this option
     * 
     * @return The associated component of this option
     */
    public JComponent getComponent() {
        return _component;
    }

    /**
     * Gets the parent component associated to this option
     * 
     * @return The parent component of this option
     */
    protected JComponent getParentComponent() {
        return _parent;
    }

    /**
     * Defines functionality for reseting the menu
     */
    protected void onReset() {
    }

    /**
     * Gets the visibility of the component
     * 
     * @return TRUE if the component is visible, FALSE otherwise
     */
    protected boolean visibility() {
        return _component.isVisible();
    }
    
    /**
     * Defines functionality for when the entry is loaded
     */
    protected void onLoad() {
    }

    /**
     * Defines functionality for initializing the menu entity
     */
    protected void onInitialize() {
    }

    @Override public final String toString() {
        return _identifier.toString();
    }

    @Override public final boolean equals(Object obj) {
        if(obj == null || !(obj instanceof AbstractMenu)) {
            return false;
        }

        AbstractMenu component = (AbstractMenu) obj;
        return component.toString() == this.toString();
    }
}