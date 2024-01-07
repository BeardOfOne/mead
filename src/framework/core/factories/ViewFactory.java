package framework.core.factories;

import framework.api.IView;

/**
 * Factory for creating and working with IView types
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class ViewFactory extends AbstractSignalFactory<IView> {
    
    @Override public <U extends IView> void remove(U resource) {
        resource.destructor();
        super.remove(resource);
    }
    
    @Override protected boolean isPersistent() {
        return false;
    }
}