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

package editor.menu;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import editor.views.PropertiesDialogView;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;
import resources.ResourceKeys;

/**
 * The properties menu item
 * 
 * @author {@literal Daniel Ricci <thedanny09@icloud.com>}
 *
 */
public class PropertiesMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public PropertiesMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.TileProperties)), parent);
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        PropertiesDialogView propertiesView = AbstractSignalFactory.getFactory(ViewFactory.class).get(PropertiesDialogView.class);
        if(propertiesView == null) {
            propertiesView = AbstractSignalFactory.getFactory(ViewFactory.class).add(new PropertiesDialogView(), true);
        }
        propertiesView.render();
    }

    @Override public boolean enabled() {
        return AbstractSignalFactory.isRunning();
    }
}