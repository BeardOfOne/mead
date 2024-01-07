package editor.controllers;

import framework.core.mvc.controller.BaseController;

import editor.models.ProjectModel;

/**
 * The project controller used to manage functionality for the project
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class ProjectController extends BaseController {

    /**
     * The project model
     */
    private final ProjectModel _model;

    /**
     * Constructs a new instance of this class type
     * 
     * @param model The properties model
     */
    public ProjectController(ProjectModel model) {
        _model = model;
    }
    
    /**
     * Gets the name of the project
     *
     * @return The name of the project
     */
    public String getProjectName() {
        return _model.getName();
    }
    
    /**
     * Sets the project name
     *
     * @param projectName The name to set the project to
     */
    public void setProjectName(String projectName) {
        _model.setProjectName(projectName);
    }
}