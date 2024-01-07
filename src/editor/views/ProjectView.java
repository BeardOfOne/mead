package editor.views;

import javax.swing.JScrollPane;

import framework.communication.internal.signal.arguments.EventArgs;
import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ControllerFactory;
import framework.core.factories.ModelFactory;
import framework.core.mvc.view.ScrollView;
import framework.core.mvc.view.layout.DraggableLayout;
import framework.core.system.Application;

import editor.controllers.ProjectController;
import editor.models.ProjectModel;

/**
 * The main window view is the outer most shell that wraps everything
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class ProjectView extends ScrollView {

    /**
     * Creates a new instance of this class type
     * 
     * @param projectName the name of the project
     */
    public ProjectView(String projectName) {
        setLayout(new DraggableLayout());
        Application.instance.setContentPane(new JScrollPane(this));
        Application.instance.setTitle(projectName);

        // Create the tile model that this view will be populated from
        ProjectModel model = AbstractFactory.getFactory(ModelFactory.class).add(
                new ProjectModel(projectName),
                true
                );
        model.addListener(this);

        getViewProperties().setEntity(
                AbstractSignalFactory.getFactory(ControllerFactory.class).add(new ProjectController(model), true)
                );
    }

    @Override public void render() {
        Application.instance.validate();
        setVisible(true);
    }

    @Override public void update(EventArgs event) {
        super.update(event);

        if(event.getSource() instanceof ProjectModel) {
            ProjectModel model = (ProjectModel) event.getSource();
            Application.instance.setTitle(model.getName());
        }

        repaint();
    }
}