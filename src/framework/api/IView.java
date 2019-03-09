/**
 * Daniel Ricci {@literal <thedanny09@icloud.com>}
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

package framework.api;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;
import java.util.logging.Level;

import framework.communication.internal.signal.ISignalListener;
import framework.communication.internal.signal.SignalListenerContainer;
import framework.core.mvc.common.CommonProperties;
import framework.utils.logging.Tracelog;

/**
 * This contract specifies how views should operate within the framework. 
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public interface IView extends ISignalListener {

    /**
     * The view properties that each IView will have
     * 
     * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
     */
    public final class ViewProperties extends CommonProperties<IController>  {

        /**
         * Indicates if the view should be redrawn
         */
        private boolean _shouldRedraw;

        /**
         * Indicates if the view should always be redrawn
         */
        private boolean _shouldAlwaysRedraw;
        
        /**
         * Constructs a new view properties object in charge of holding
         * information about the view such as the controller
         * 
         * @param view A reference to the container class implementing the view.
         * 
         * Note: The parameter exists because from an interface there is no way
         * to get a reference to the outer class using Outer.this.methodName
         */
        public ViewProperties(IView view) {

            // The view is set to false because by default most container classes
            // are already visible when they are created and because of this no event
            // will ever be sent out that a JPanel or some other container is being shown
            //
            // Note: The render at this point in time is now in charge of setting a container
            // class to visible, but the implementor must call super
            // 
            // Note: Do not touch this line of code, ever.
            view.getContainerClass().setVisible(false);

            // Register the view to listen in on the event when it becomes hidden/shown, this
            // is done so that registration can happen on signals
            //
            // Note: This needs to be done after setting the container's visibility to false
            // or there will be a false positive for the hidden event
            view.getContainerClass().addComponentListener(new ComponentAdapter() {

                // This event is fired when a view is hidden, that is when
                // the visibility is set to false
                @Override public void componentHidden(ComponentEvent args) {
                    // log that the component is being hidden
                    Tracelog.log(Level.INFO, false, String.format("Component %s is being hidden", args.getSource().getClass().getName()));
                }

                @Override public void componentShown(ComponentEvent args) {
                    // Indicate that this view is being shown now
                    Tracelog.log(Level.INFO, false, String.format("Component %s is being shown", args.getSource().getClass().getCanonicalName()));
                }
            });
        }

        /**
         * Gets a flag indicating if the view should be redrawn
         * 
         * @return TRUE if the view should be redrawn
         */
        public final boolean shouldRedraw() {
            return _shouldRedraw;
        }
        
        public final boolean shouldAlwaysRedraw() {
            return _shouldAlwaysRedraw;
        }

        /**
         * Sets if the view should be redrawn
         * 
         * @param shouldRedraw If the view should be redrawn
         */
        public final void setRedraw(boolean shouldRedraw) {
            _shouldRedraw = shouldRedraw;
        }
        
        public final void setShouldAlwaysRedraw(boolean shouldAlwaysRedraw) {
            _shouldAlwaysRedraw = shouldAlwaysRedraw;
        }
    }

    /**
     * Gets a reference to the view properties associated to the view
     * 
     * @return the view properties associated to the view
     */
    public ViewProperties getViewProperties();

    /**
     * Gets the containing class of the view, this is the container representation in Swing terms
     * 
     * @param <T> A type extending the class {@link Container}
     * 
     * @return The swing container of the view
     */
    default public <T extends Container> T getContainerClass() {
        return (T)this;
    }

    default public IView getParentIView() {
        Container container = this.getContainerClass();
        while(container != null && !((container = container.getParent()) instanceof IView)) {
        }
        
        if(container == this) {
            return null;
        }
        
        return (IView)container;
    }
    
    /**
     * Renders the view
     */
    public default void render() {
        getContainerClass().setVisible(true);
        for(Component component : getContainerClass().getComponents()) {
            if(component instanceof IView) {
                ((IView)component).render();
            }
        }
    }
    
    /**
     * Called before the view is removed from the underlying 
     */
    public default void destructor() {
    }
    
    @Override default Map<String, SignalListenerContainer> getSignals() {
        return getViewProperties().getSignalListeners();
    }
}