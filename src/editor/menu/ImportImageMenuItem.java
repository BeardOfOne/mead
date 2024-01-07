package editor.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import framework.communication.external.builder.Director;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.persistance.builder.ImportImageBuilder;
import resources.ResourceKeys;

/**
 * The export menu item 
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class ImportImageMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public ImportImageMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.Image)), parent);
        getComponent(JMenuItem.class).setAccelerator(KeyStroke.getKeyStroke('I', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
    }

    @Override public void onExecute(ActionEvent actionEvent) {

        // Create the builder that will be constructed
        ImportImageBuilder builder = new ImportImageBuilder();

        // Create a director for importing
        Director importDirector = new Director(builder);
        importDirector.construct();
    }

    @Override public boolean isEnabled() {
        return true;
    }
}