/**
 * Daniel Ricci <thedanny09@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package editor.menu;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import editor.controllers.ProjectController;
import editor.views.ProjectDialogView;
import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ControllerFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;
import resources.ResourceKeys;

/**
 * The menu item associated to the creation of a new project
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
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

    @Override public boolean enabled() {
        return AbstractFactory.isRunning();
    }
}