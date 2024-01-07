package editor.menu;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ControllerFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.controllers.ProjectController;
import editor.views.ProjectDialogView;
import resources.ResourceKeys;

/**
 * The menu item associated to the creation of a new project
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class ProjectSettingsMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component associated to this menu entity
     */
    public ProjectSettingsMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.ProjectSettings)), parent);
    }

    @Override public void onExecute(ActionEvent actionEvent) {

        // Create a new setup dialog
        ProjectDialogView projectDialog = AbstractSignalFactory.getFactory(ViewFactory.class).add(new ProjectDialogView(), true);
        
        ProjectController projectController = AbstractFactory.getFactory(ControllerFactory.class).get(ProjectController.class);
        projectDialog.setNameField(AbstractFactory.getFactory(ControllerFactory.class).get(ProjectController.class).getProjectName());
        projectDialog.render();

        // If the dialog is not valid then clear the contents of the application and go no further
        if(projectDialog.getDialogResult() != JOptionPane.OK_OPTION) {
            return;
        }
        
        // Set the new name of the project
        projectController.setProjectName(projectDialog.getNameField());
    }

    @Override public boolean isEnabled() {
        return AbstractFactory.isRunning();
    }
}