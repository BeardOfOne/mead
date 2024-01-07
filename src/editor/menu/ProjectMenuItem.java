package editor.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.views.ProjectDialogView;
import editor.views.ProjectView;
import resources.ResourceKeys;

/**
 * The menu item associated to the creation of a new project
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class ProjectMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component associated to this menu entity
     */
    public ProjectMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.NewProject)), parent);
        super.getComponent(JMenuItem.class).setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        ProjectDialogView projectView = new ProjectDialogView();
        projectView.render();

        if(projectView.getDialogResult() != JOptionPane.OK_OPTION) {
            return;
        }

        AbstractSignalFactory.clearFactories();
        AbstractSignalFactory.getFactory(ViewFactory.class).add(projectView, true);

        ProjectView view = AbstractSignalFactory.getFactory(ViewFactory.class).add(new ProjectView(projectView.getNameField()), true);
        view.render();
    }

    @Override public boolean isEnabled() {
        return true;
    }
}