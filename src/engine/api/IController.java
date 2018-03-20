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

package engine.api;

import java.util.Map;

import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.SignalListenerContainer;
import engine.core.factories.AbstractFactory;
import engine.core.mvc.IDestructor;
import engine.core.mvc.common.CommonProperties;
import game.core.factories.ControllerFactory;

/**
 * This is the top-most controller interface, for all sub-type implemented controller types
 * and interfaces
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public interface IController extends IDestructor, ISignalListener {

    /**
     * This class represents the controller properties of each IController implemented type
     * 
     * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
     *
     */
    public final class ControllerProperties extends CommonProperties<IView> {
    }

    /**
     * Gets the controller properties associated to the type
     * 
     * @return The controller properties
     */
    public ControllerProperties getControllerProperties();

    @Override default Map<String, SignalListenerContainer> getSignalListeners() {
        return getControllerProperties().getSignalListeners();
    }

    @Override default void remove() {
        IDestructor.super.remove();

        // Clear the signals associated to this controller
        clearSignalListeners();

        // Remove this controller from the controller factory
        AbstractFactory.getFactory(ControllerFactory.class).remove(this);
    }
}