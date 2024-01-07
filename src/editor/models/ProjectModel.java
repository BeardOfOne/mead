
package editor.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import framework.communication.internal.signal.arguments.EventArgs;
import framework.core.mvc.model.BaseModel;


/**
 * This model represents the contents of a project.
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="project")
public final class ProjectModel extends BaseModel {

    /**
     * The name of the project
     */
    @XmlAttribute(name="name")
    private String _projectName = "";

    /**
     * Constructs a new instance of this class type
     *
     * Note: Explicit default constructor
     */
    public ProjectModel() {
    }

    /**
     * 
     * Constructs a new instance of this class type
     *
     * @param name The name of the project
     */
    public ProjectModel(String name) {
        _projectName = name;
    }

    /**
     * Gets the name of the project
     * 
     * @return The name of the project
     */
    @Override
    public String getName() {
        return _projectName;
    }
    
    /**
     * Sets the name of the project
     *
     * @param projectName The name of the project
     */
    public void setProjectName(String projectName) {
        _projectName = projectName;
        doneUpdating();
    }

    @Override public String toString() {
        return _projectName;
    }

    @Override public void update(EventArgs signalEvent) {
    }
}