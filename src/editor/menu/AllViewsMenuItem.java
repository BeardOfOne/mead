package editor.menu;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import framework.core.factories.AbstractSignalFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.core.navigation.MenuBuilder;
import framework.core.system.Application;
import framework.utils.globalisation.Localization;

import resources.ResourceKeys;

/**
 * The properties menu item
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class AllViewsMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public AllViewsMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.AllViews)), parent);
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        MenuBuilder.search(Application.instance.getJMenuBar(), TileLayersMenuItem.class).onExecute(actionEvent);
        MenuBuilder.search(Application.instance.getJMenuBar(), PropertiesMenuItem.class).onExecute(actionEvent);
    }

    @Override public boolean isEnabled() {
        return AbstractSignalFactory.isRunning();
    }
}