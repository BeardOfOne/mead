package editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import framework.core.navigation.AbstractMenuItem;
import framework.core.system.Application;
import framework.utils.globalisation.Localization;

import resources.ResourceKeys;

/**
 * The exit menu item
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class ExitMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public ExitMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.Exit)), parent);
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        Application.instance.dispatchEvent(
                new WindowEvent(Application.instance, WindowEvent.WINDOW_CLOSING)
                );
    }

    @Override public boolean isEnabled() {
        return true;
    }
}