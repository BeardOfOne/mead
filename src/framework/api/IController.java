package framework.api;

import java.util.Map;

import framework.communication.internal.signal.ISignalListener;
import framework.communication.internal.signal.SignalListenerContainer;
import framework.core.mvc.common.CommonProperties;

/**
 * This is the top-most controller interface, for all sub-type implemented controller types
 * and interfaces
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public interface IController extends ISignalListener {

    /**
     * This class represents the controller properties of each IController implemented type
     * 
     * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
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

    @Override default Map<String, SignalListenerContainer> getSignals() {
        return getControllerProperties().getSignalListeners();
    }
}