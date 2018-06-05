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

package engine.core.mvc.controller;

import java.util.List;

import engine.api.IController;
import engine.api.IModel;
import engine.communication.internal.signal.ISignalListener;
import engine.communication.internal.signal.arguments.AbstractEventArgs;

/**
 * Top-level controller class that holds common controller information
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public abstract class BaseController implements IController  {

    /**
     * The controller properties associated to this controller
     */
    private final ControllerProperties _properties = new ControllerProperties();

    /**
     * Constructs a new instance of this class type
     */
    public BaseController() {
    }

    @Override public final void addSignalListener(ISignalListener listener) {
        List<IModel> models = getControllerModels();
        if(models != null) {
            for(IModel model : getControllerModels()) {
                model.addListeners(listener);
            }
        }
    }
    
    protected abstract List<IModel> getControllerModels();
      
    @Override public final ControllerProperties getControllerProperties() {
        return _properties;
    }
    
    @Override public void update(AbstractEventArgs signalEvent) { }
    
    @Override public void registerSignalListeners() { }
    
    @Override public void clear() { }
}