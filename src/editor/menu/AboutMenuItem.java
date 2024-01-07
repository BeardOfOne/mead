package editor.menu;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import resources.ResourceKeys;

/**
 * The about menu item
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class AboutMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public AboutMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.About)), parent);
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(
                null,
                Localization.instance().getLocalizedString(ResourceKeys.Github),
                Localization.instance().getLocalizedString(ResourceKeys.About),
                JOptionPane.INFORMATION_MESSAGE
                );
    }
}