package editor.menu;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.views.PropertiesDialogView;
import resources.ResourceKeys;

/**
 * The properties menu item
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
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

    @Override public boolean isEnabled() {
        return AbstractSignalFactory.isRunning();
    }
}