package framework.core.mvc.controller;

import framework.api.IController;
import framework.communication.internal.signal.arguments.EventArgs;

/**
 * Top-level controller class that holds common controller information
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public abstract class BaseController implements IController  {

    /**
     * The controller properties associated to this controller
     */
    private final ControllerProperties _properties = new ControllerProperties();

    /**
     * @return The controller models associated to this controller
     */
    @Override public final ControllerProperties getControllerProperties() {
        return _properties;
    }
    
    @Override public void update(EventArgs signalEvent) { }
}