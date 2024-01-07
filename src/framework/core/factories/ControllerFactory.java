package framework.core.factories;

import framework.api.IController;

/**
 * Factory for creating and working with IController types
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class ControllerFactory extends AbstractSignalFactory<IController> {
    
    @Override protected boolean isPersistent() {
        return false;
    }
}