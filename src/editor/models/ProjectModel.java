/**
 * Daniel Ricci <thedanny09@icloud.com>
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

package editor.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import framework.communication.internal.signal.arguments.AbstractEventArgs;
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

    @Override public void update(AbstractEventArgs signalEvent) {
    }
}