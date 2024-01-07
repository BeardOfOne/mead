package editor.menu;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.views.LayersDialogView;
import resources.ResourceKeys;

/**
 * The tile layers menu item
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class TileLayersMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public TileLayersMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.TileLayers)), parent);
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        LayersDialogView view = AbstractSignalFactory.getFactory(ViewFactory.class).get(LayersDialogView.class);
        if(view == null) {
            view = AbstractSignalFactory.getFactory(ViewFactory.class).add(new LayersDialogView(), true);
        }
        view.render();
    }

    @Override public boolean isEnabled() {
        return AbstractSignalFactory.isRunning();
    }
}