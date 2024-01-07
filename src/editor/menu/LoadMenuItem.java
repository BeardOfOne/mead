package editor.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import framework.communication.external.builder.Director;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.persistance.builder.LoadTileMapBuilder;
import resources.ResourceKeys;

/**
 * The load menu item
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class LoadMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public LoadMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.Load)), parent);
        getComponent(JMenuItem.class).setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        Director loadDirector = new Director(new LoadTileMapBuilder());
        loadDirector.construct();
    }

    @Override public boolean isEnabled() {
        return true;
    }
}