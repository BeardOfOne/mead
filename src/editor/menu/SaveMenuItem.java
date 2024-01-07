package editor.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import framework.communication.external.builder.Director;
import framework.core.factories.AbstractSignalFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.persistance.builder.SaveTileMapBuilder;
import resources.ResourceKeys;

/**
 * The save menu item 
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class SaveMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public SaveMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.Save)), parent);
        getComponent(JMenuItem.class).setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        Director saveDirector = new Director(new SaveTileMapBuilder());
        saveDirector.construct();
    }

    @Override public boolean isEnabled() {
        return AbstractSignalFactory.isRunning();
    }	
}