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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.views.TileMapDialogView;
import editor.views.TileMapView;
import resources.ResourceKeys;

/**
 * The menu item for the process of creating a new tile map entry
 * 
 * @author {@literal Daniel Ricci <thedanny09@icloud.com>}
 *
 */
public class TileMapMenuItem extends AbstractMenuItem {

    private Dimension _dimensions;

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public TileMapMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.NewTilemap)), parent);
        super.getComponent(JMenuItem.class).setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
    }

    public void setDimensions(Dimension dimensions) {
        _dimensions = dimensions;
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        // Create a new dialog
        TileMapDialogView dialog = AbstractSignalFactory.getFactory(ViewFactory.class).add(new TileMapDialogView(_dimensions), false);
        dialog.render();

        // If the user did not select the OK button then remove the contents of the dialog
        if(dialog.getDialogResult() != JOptionPane.OK_OPTION) {
            return;
        }

        // Create a new tilemap view and render it's contents
        TileMapView tilemapView = AbstractSignalFactory.getFactory(ViewFactory.class).add(
                new TileMapView(
                        dialog.getNameField(),
                        dialog.getRowsField(),
                        dialog.getColumnsField(),
                        dialog.getCellWidthField(),
                        dialog.getCellHeightField()
                        ), 
                false
                );
        
        // Render the contents of the tilemap
        tilemapView.render();
    }

    @Override public boolean isEnabled() {
        return AbstractFactory.isRunning();
    }

    @Override public void onReset() {
        _dimensions = null;
    }

}