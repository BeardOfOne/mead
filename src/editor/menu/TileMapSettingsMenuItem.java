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

package editor.menu;

import java.awt.event.ActionEvent;
import java.util.Optional;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ModelFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.models.TileMapModel;
import editor.views.TileMapDialogView;
import resources.ResourceKeys;

/**
 * The menu item associated to editing a tilemap
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class TileMapSettingsMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component associated to this menu entity
     */
    public TileMapSettingsMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.TileMapSettings)), parent);
    }

    @Override public void onExecute(ActionEvent actionEvent) {

        // Get a reference to the currently selected tile map
        Optional<TileMapModel> tileMapModel = AbstractFactory.getFactory(ModelFactory.class).getAll(TileMapModel.class).stream().filter(z -> z.getSelected()).findFirst();
        if(!tileMapModel.isPresent()) {
            return;
        }
        
        // Create the tile map dialog and populate it with the currently selected model
        TileMapDialogView tileMapDialogView = AbstractSignalFactory.getFactory(ViewFactory.class).add(new TileMapDialogView(
                tileMapModel.get().getName(),
                Integer.toString(tileMapModel.get().getRows()),
                Integer.toString(tileMapModel.get().getColumns()),
                Integer.toString(tileMapModel.get().getWidth()),
                Integer.toString(tileMapModel.get().getHeight())), 
        true);
        
        // Render the dialog view
        tileMapDialogView.render();
        
        // If the dialog OK button was selected then proceed forward with the changes
        if(tileMapDialogView.getDialogResult() == JOptionPane.OK_OPTION) {
            tileMapModel.get().setName(tileMapDialogView.getNameField());
            tileMapModel.get().setWidth(tileMapDialogView.getCellWidthField());
            tileMapModel.get().setHeight(tileMapDialogView.getCellHeightField());
        }
    }
    
    @Override protected boolean isEnabled() {
        return true;
    }

    @Override protected boolean visibility() {
        return AbstractFactory.isRunning() && AbstractFactory.getFactory(ModelFactory.class).getAll(TileMapModel.class).stream().anyMatch(z -> z.getSelected());
    }
}