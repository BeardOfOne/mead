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

package framework.core.mvc.view;

import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import framework.api.IController;
import framework.api.IView;

/**
 * A simple dialog that extends from the default swing dialog implementation
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public abstract class DialogView extends JDialog implements IView {

    /**
     * Flag indicating if this dialog should always attempt to center itself to it's parent component
     */
    private boolean _isAutomaticDialogCentering = false;
    
    /**
     * The dialog result state of this dialog
     */
    private int _dialogResult = JOptionPane.DEFAULT_OPTION;

    /**
     * The view properties of this view
     */
    private final ViewProperties _properties = new ViewProperties(this);

    /**
     * Constructs a new instance of this class
     * 
     * @param parent The parent of this dialog
     * @param title The title for this dialog
     * 
     * @param <T> A type extending a class that implements the {@link IController} interface
     */
    public <T extends IController> DialogView(Window parent, String title) {
        super(parent, title);
    }

    /**
     * Constructs a new instance of this class
     * 
     * @param parent The parent of this dialog
     * @param title The title for this dialog
     * @param width The initial width of this dialog
     * @param height The initial height of this dialog
     */
    public DialogView(Window parent, String title, int width, int height) {
        this(parent, title);

        // Set the size of this dialog
        setSize(width, height);
    }

    /**
     * Method for validating that a form is valid
     * 
     * @return If the dialog is valid 
     */
    protected boolean validateDialog() {
        return true;
    }

    /**
     * Gets the dialog result status.
     * 
     * Note: This should correspond to a JOptionPane result
     * 
     * @return The status of the dialog result
     */
    public final int getDialogResult() { 
        return _dialogResult; 
    }

    /**
     * Sets the dialog result of this dialog
     * Note: This should correspond to a JOptionPane result
     * 
     * @param result The result to set as the dialog result
     */
    public final void setDialogResult(int result) {
        _dialogResult = result;
    }
    
    /**
     * Sets the flag indicating if this dialog should attempt to center itself to it's owner when rendered 
     * 
     * Note: false by default
     *
     * @param isAutomaticDialogCentering TRUE if this dialog should attempt to center itself to it's owner when rendered, FALSE otherwise
     */
    public final void setAutomaticDialogCentering(boolean isAutomaticDialogCentering) {
        _isAutomaticDialogCentering = isAutomaticDialogCentering;
    }
    
    @Override public void render() {
        pack();
        if(_isAutomaticDialogCentering) {
            Rectangle ownerScreen = this.getOwner().getGraphicsConfiguration().getBounds();
            setLocation(
                ownerScreen.x + (ownerScreen.width - getWidth()) / 2,
                ownerScreen.y + (ownerScreen.height - getHeight()) / 2
            );
        }
        IView.super.render();
    }

    @Override public final ViewProperties getViewProperties() {
        return _properties;
    }
}